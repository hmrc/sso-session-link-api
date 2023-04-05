import play.sbt.PlayImport.PlayKeys.playDefaultPort
import sbt.Keys._
import sbt._
import uk.gov.hmrc.DefaultBuildSettings._
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion

lazy val microservice = Project("sso-session-link-api", file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .settings(scalaSettings: _*)
  .settings(defaultSettings(): _*)
  .settings(inConfig(IntegrationTest)(Defaults.itSettings): _*)
  .settings(
    majorVersion := 0,
    scalaVersion := "2.13.8",
    scalacOptions ++= Seq("-Xfatal-warnings", "-feature"),
    libraryDependencies ++= AppDependencies(),
      Test / parallelExecution := false,
      Test / fork := false,
    retrieveManaged := true,
    integrationTestSettings(),
    ScoverageSettings(),
    SilencerSettings(),
    playDefaultPort := 9977,
    scalafmtOnCompile := true
  )
  .configs(IntegrationTest)
  .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)
