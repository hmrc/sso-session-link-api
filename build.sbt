import play.sbt.PlayImport.PlayKeys.playDefaultPort
import sbt.Keys.*
import sbt.*
import uk.gov.hmrc.DefaultBuildSettings
import uk.gov.hmrc.DefaultBuildSettings.*
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion

ThisBuild / majorVersion := 0
ThisBuild / scalaVersion := "3.5.0"

lazy val microservice = Project("sso-session-link-api", file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .settings(scalaSettings *)
  .settings(defaultSettings() *)
  .settings(
    scalacOptions += "-Wconf:msg=unused import&src=html/.*:s",
    scalacOptions += "-Wconf:msg=unused import&src=routes/.*:s",
    scalacOptions += "-Wconf:src=routes/.*:s",
    libraryDependencies ++= AppDependencies(),
      Test / parallelExecution := false,
      Test / fork := false,
    retrieveManaged := true,
    ScoverageSettings(),
    playDefaultPort := 9977,
    scalafmtOnCompile := true
  )
  .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)

lazy val it = project
  .enablePlugins(PlayScala)
  .dependsOn(microservice % "test->test") // the "test->test" allows reusing test code and test dependencies
  .settings(DefaultBuildSettings.itSettings())
