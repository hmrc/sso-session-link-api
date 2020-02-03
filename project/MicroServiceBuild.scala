import sbt._

object MicroServiceBuild extends Build with MicroService {

  import scala.util.Properties.envOrElse

  val appName = "sso-session-link-api"
  val appVersion = envOrElse("SSO_VERSION", "999-SNAPSHOT")
  override lazy val appDependencies: Seq[ModuleID] = AppDependencies()
  override lazy val overrides = AppDependencies.overrides
}

private object AppDependencies {

  import play.sbt.PlayImport._
  import play.core.PlayVersion

  val compile = Seq(
    ws,
    guice,
    "uk.gov.hmrc" %% "government-gateway-domain" % "2.5.0" exclude("uk.gov.hmrc", "http-verbs_2.11"),
    "uk.gov.hmrc" %% "bootstrap-play-26" % "1.3.0"
  )

  val test = Seq(
    "uk.gov.hmrc" %% "government-gateway-test" % "2.5.0-play-26" % "test,it",
    "org.mockito" %% "mockito-scala-scalatest" % "1.5.13" % "test,it",
    "com.github.tomakehurst" % "wiremock" % "2.24.1" % "it"
  )

  val overrides: Set[ModuleID] = {
    val jettyFromWiremockVersion = "9.4.15.v20190215"
    Set(
      "org.eclipse.jetty"           % "jetty-client"       % jettyFromWiremockVersion % "it",
      "org.eclipse.jetty"           % "jetty-continuation" % jettyFromWiremockVersion % "it",
      "org.eclipse.jetty"           % "jetty-http"         % jettyFromWiremockVersion % "it",
      "org.eclipse.jetty"           % "jetty-io"           % jettyFromWiremockVersion % "it",
      "org.eclipse.jetty"           % "jetty-security"     % jettyFromWiremockVersion % "it",
      "org.eclipse.jetty"           % "jetty-server"       % jettyFromWiremockVersion % "it",
      "org.eclipse.jetty"           % "jetty-servlet"      % jettyFromWiremockVersion % "it" ,
      "org.eclipse.jetty"           % "jetty-servlets"     % jettyFromWiremockVersion % "it",
      "org.eclipse.jetty"           % "jetty-util"         % jettyFromWiremockVersion % "it",
      "org.eclipse.jetty"           % "jetty-webapp"       % jettyFromWiremockVersion % "it",
      "org.eclipse.jetty"           % "jetty-xml"          % jettyFromWiremockVersion % "it",
      "org.eclipse.jetty.websocket" % "websocket-api"      % jettyFromWiremockVersion % "it",
      "org.eclipse.jetty.websocket" % "websocket-client"   % jettyFromWiremockVersion % "it",
      "org.eclipse.jetty.websocket" % "websocket-common"   % jettyFromWiremockVersion % "it"
    )
  }

  def apply() = compile ++ test
}
