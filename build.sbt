import play.sbt.PlayImport.PlayKeys
import sbt.Keys._
import sbt._
import uk.gov.hmrc.DefaultBuildSettings._
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin._
import uk.gov.hmrc.versioning.SbtGitVersioning
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion
import uk.gov.hmrc.SbtAutoBuildPlugin

lazy val microservice = Project("sso-session-link-api", file("."))
  .settings(majorVersion := 0)
  .enablePlugins(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin)
  .settings(scalaSettings: _*)
  .settings(scalaVersion := "2.12.11")
  .settings(scalacOptions ++= Seq("-Xfatal-warnings", "-feature"))
  .settings(publishingSettings: _*)
  .settings(defaultSettings(): _*)
  .settings(
    targetJvm := "jvm-1.8",
    libraryDependencies ++= AppDependencies(),
    parallelExecution in Test := false,
    fork in Test := false,
    retrieveManaged := true
  )
  .configs(IntegrationTest)
  .settings(inConfig(IntegrationTest)(Defaults.itSettings): _*)
  .settings(integrationTestSettings())
  .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)
  .settings(ScalariformSettings())
  .settings(ScoverageSettings())
  .settings(SilencerSettings())
  .settings(PlayKeys.playDefaultPort := 9977)
