import sbt._

object MicroServiceBuild extends Build with MicroService {

  import scala.util.Properties.envOrElse

  val appName = "sso-session-link-api"
  val appVersion = envOrElse("SSO_VERSION", "999-SNAPSHOT")
  override lazy val appDependencies: Seq[ModuleID] = AppDependencies()
}

private object AppDependencies {

  import play.sbt.PlayImport._
  import play.core.PlayVersion

  val compile = Seq(
    ws,
    "uk.gov.hmrc" %% "domain" % "5.3.0",
    "uk.gov.hmrc" %% "play-hmrc-api" % "3.4.0-play-25",
    "uk.gov.hmrc" %% "government-gateway-domain" % "1.32.0",
    "uk.gov.hmrc" %% "bootstrap-play-25" % "5.1.0"
  )

  val test = Seq(
    "uk.gov.hmrc" %% "hmrctest" % "3.9.0-play-25" % "test,it",
    "org.scalatest" %% "scalatest" % "3.0.0" % "test,it",
    "com.typesafe.play" %% "play-test" % PlayVersion.current % "test,it",
    "org.scalatestplus.play" % "scalatestplus-play_2.11" % "2.0.1" % "test,it",
    "uk.gov.hmrc" %% "government-gateway-test" % "1.7.0" % "it",
    "org.mockito" %% "mockito-scala-scalatest" % "1.5.13" % "test,it",
    "com.github.tomakehurst" % "wiremock" % "2.24.1" % "it"
  )

  def apply() = compile ++ test
}
