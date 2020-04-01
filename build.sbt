import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import play.sbt.PlayImport.PlayKeys
import sbt.Keys._
import sbt._
import scalariform.formatter.preferences._
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin._
import uk.gov.hmrc.versioning.SbtGitVersioning
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion
import uk.gov.hmrc.{SbtArtifactory, SbtAutoBuildPlugin}
import uk.gov.hmrc.DefaultBuildSettings._

lazy val scalariformSettings = {
  // description of options found here -> https://github.com/scala-ide/scalariform
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(AlignArguments, true)
    .setPreference(AlignParameters, true)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(AllowParamGroupsOnNewlines, true)
    .setPreference(CompactControlReadability, false)
    .setPreference(CompactStringConcatenation, false)
    .setPreference(DanglingCloseParenthesis, Preserve)
    .setPreference(DoubleIndentConstructorArguments, true)
    .setPreference(DoubleIndentMethodDeclaration, true)
    .setPreference(FirstArgumentOnNewline, Preserve)
    .setPreference(FirstParameterOnNewline, Preserve)
    .setPreference(FormatXml, true)
    .setPreference(IndentLocalDefs, true)
    .setPreference(IndentPackageBlocks, true)
    .setPreference(IndentSpaces, 2)
    .setPreference(IndentWithTabs, false)
    .setPreference(MultilineScaladocCommentsStartOnFirstLine, false)
    .setPreference(NewlineAtEndOfFile, true)
    .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, false)
    .setPreference(PreserveSpaceBeforeArguments, true)
    .setPreference(RewriteArrowSymbols, false)
    .setPreference(SpaceBeforeColon, false)
    .setPreference(SpaceBeforeContextColon, false)
    .setPreference(SpaceInsideBrackets, false)
    .setPreference(SpaceInsideParentheses, false)
    .setPreference(SpacesAroundMultiImports, false)
    .setPreference(SpacesWithinPatternBinders, true)
}

lazy val scoverageSettings = {
  import scoverage.ScoverageKeys
  Seq(
    // Semicolon-separated list of regexs matching classes to exclude
    ScoverageKeys.coverageExcludedPackages :=
      """<empty>;
        |Reverse.*;
        |.*?(config|views.*);
        |.*?(AuthService|BuildInfo|Routes).*;
        |.*?DocumentationController.*;
        |.*?SandboxController.*;
        |.*?GuiceModule.*;
        |.*?microserviceGlobal.*;
        |.*?microserviceWiring
        |""".stripMargin,
    ScoverageKeys.coverageMinimum := 80,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true,
    parallelExecution in Test := false
  )
}

lazy val microservice = Project("sso-session-link-api", file("."))
  .settings(majorVersion := 0)
  .enablePlugins(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin, SbtArtifactory)
  .settings(scoverageSettings)
  .settings(scalaSettings: _*)
  .settings(scalaVersion := "2.12.11")
  .settings(scalacOptions ++= Seq("-Xfatal-warnings", "-feature"))
  .settings(SilencerSettings())
  .settings(publishingSettings: _*)
  .settings(defaultSettings(): _*)
  .settings(scalariformSettings)
  .settings(
    targetJvm := "jvm-1.8",
    libraryDependencies ++= AppDependencies(),
    dependencyOverrides ++= AppDependencies.overrides,
    parallelExecution in Test := false,
    fork in Test := false,
    retrieveManaged := true
  )
  .configs(IntegrationTest)
  .settings(inConfig(IntegrationTest)(Defaults.itSettings): _*)
  .settings(integrationTestSettings())
  .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)
  .settings(resolvers += Resolver.bintrayRepo("hmrc", "releases"))
  .settings(resolvers += "hmrc-releases" at "https://artefacts.tax.service.gov.uk/artifactory/hmrc-releases/")
  .settings(PlayKeys.playDefaultPort := 9977)
