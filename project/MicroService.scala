import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import play.sbt.PlayImport.PlayKeys
import play.sbt.PlayImport.PlayKeys._
import sbt.Keys._
import sbt.Tests.{Group, SubProcess}
import sbt._
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin._
import play.sbt.routes.RoutesKeys.routesGenerator
import play.sbt.routes.RoutesKeys._
import scalariform.formatter.preferences._
import uk.gov.hmrc.ServiceManagerPlugin.serviceManagerSettings
import uk.gov.hmrc.ServiceManagerPlugin.Keys._
import wartremover.{Wart, wartremoverErrors, wartremoverExcluded, wartremoverWarnings}
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning
import uk.gov.hmrc.SbtArtifactory

trait MicroService {

  import uk.gov.hmrc._
  import uk.gov.hmrc.DefaultBuildSettings.{oneForkedJvmPerTest => _, _}
  import uk.gov.hmrc.{SbtBuildInfo, ShellPrompt}

  import TestPhases._

  val appName: String

  lazy val appDependencies: Seq[ModuleID] = ???
  lazy val plugins: Seq[Plugins] = Seq(play.sbt.PlayScala, SbtDistributablesPlugin)
  lazy val playSettings: Seq[Setting[_]] = Seq.empty

  lazy val externalServices = List(
    ExternalService(name = "EXTERNAL_PORTAL_STUB"),
    ExternalService(name = "AUTH", enableTestOnlyEndpoints = true),
    ExternalService(name = "OPENID_CONNECT_IDTOKEN"),
    ExternalService(name = "AUTH_LOGIN_STUB"),
    ExternalService(name = "GG", enableTestOnlyEndpoints = true),
    ExternalService(name = "AUTH_LOGIN_API"),
    ExternalService(name = "USER_DETAILS"),
    ExternalService(name = "IDENTITY_VERIFICATION")
  )

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

  lazy val wartRemoverWarning = {
    // Warnings
    val warningWarts = Seq(
      //Wart.JavaSerializable,
      Wart.NonUnitStatements,
      Wart.StringPlusAny,
      Wart.AsInstanceOf,
      Wart.IsInstanceOf,
      Wart.DefaultArguments,
      Wart.FinalCaseClass,
      Wart.Nothing
    )
    wartremoverWarnings in (Compile, compile) ++= warningWarts
  }

  lazy val wartRemoverError = {
    // Error
    val errorWarts = Seq(
      Wart.ArrayEquals,
      Wart.Any,
      Wart.AnyVal,
      Wart.EitherProjectionPartial,
      Wart.Enumeration,
      Wart.ExplicitImplicitTypes,
      Wart.FinalVal,
      Wart.JavaConversions,
      Wart.JavaSerializable,
      Wart.LeakingSealed,
      Wart.MutableDataStructures,
      Wart.Null,
      Wart.OptionPartial,
      Wart.Recursion,
      Wart.Return,
      Wart.TraversableOps,
      Wart.TryPartial,
      Wart.Var,
      Wart.While)

    wartremoverErrors in (Compile, compile) ++= errorWarts
  }

  lazy val scoverageSettings = {
    import scoverage.ScoverageKeys
    Seq(
      // Semicolon-separated list of regexs matching classes to exclude
      ScoverageKeys.coverageExcludedPackages := "<empty>;Reverse.*;.*(config|views.*);.*(AuthService|BuildInfo|Routes).*",
      ScoverageKeys.coverageMinimum := 55,
      ScoverageKeys.coverageFailOnMinimum := true,
      ScoverageKeys.coverageHighlighting := true,
      parallelExecution in Test := false
    )
  }

  lazy val microservice = Project(appName, file("."))
    .enablePlugins(plugins: _*)
    .settings(majorVersion:= 0)
    .enablePlugins(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin, SbtArtifactory)
    .settings(playSettings: _*)
    .settings(scoverageSettings)
    .settings(scalaSettings: _*)
    .settings(publishingSettings: _*)
    .settings(defaultSettings(): _*)
    .settings(scalariformSettings)
    .settings(wartRemoverError)
    .settings(wartRemoverWarning)
    .settings(wartremoverExcluded ++=
      routes.in(Compile).value ++
        (baseDirectory.value ** "*.sc").get ++
        (baseDirectory.value ** "HealthCheck.scala").get ++
        (baseDirectory.value ** "HealthCheckRunner.scala").get ++
        (baseDirectory.value ** "Lock.scala").get ++
        (baseDirectory.value / "it" ** "*.*").get ++
        (baseDirectory.value / "test" ** "*.*").get ++
        Seq(sourceManaged.value / "main" / "sbt-buildinfo" / "BuildInfo.scala")
    )
    .settings(
      targetJvm := "jvm-1.8",
      scalaVersion := "2.11.11",
      libraryDependencies ++= appDependencies,
      parallelExecution in Test := false,
      fork in Test := false,
      retrieveManaged := true
    )
    .settings(Repositories.playPublishingSettings: _*)
    .configs(IntegrationTest)
    .settings(inConfig(IntegrationTest)(Defaults.itSettings): _*)
    .settings(serviceManagerSettings: _*)
    .settings(itDependenciesList := externalServices)
    .settings(
      Keys.fork in IntegrationTest := false,
      unmanagedSourceDirectories in IntegrationTest <<= (baseDirectory in IntegrationTest) (base => Seq(base / "it")),
      addTestReportOption(IntegrationTest, "int-test-reports"),
      testGrouping in IntegrationTest := oneForkedJvmPerTest((definedTests in IntegrationTest).value),
      parallelExecution in IntegrationTest := false)
    .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)
    .settings(resolvers += Resolver.bintrayRepo("hmrc", "releases"))
    .settings(resolvers += "hmrc-releases" at "https://artefacts.tax.service.gov.uk/artifactory/hmrc-releases/")
    .settings(PlayKeys.playDefaultPort := 9977)
}

private object TestPhases {

  def oneForkedJvmPerTest(tests: Seq[TestDefinition]) =
    tests.map { test => Group(test.name, Seq(test), SubProcess(ForkOptions(runJVMOptions = Seq("-Dtest.name=" + test.name)))) }
}

private object Repositories {

  import uk.gov.hmrc._
  import PublishingSettings._

  lazy val playPublishingSettings: Seq[sbt.Setting[_]] = sbtrelease.ReleasePlugin.releaseSettings ++ Seq(
    credentials += SbtCredentials,
    publishArtifact in(Compile, packageDoc) := false,
    publishArtifact in(Compile, packageSrc) := false
  ) ++
    publishAllArtefacts
}
