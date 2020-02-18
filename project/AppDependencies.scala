import sbt._



object AppDependencies {

  import play.sbt.PlayImport._

  val compile = Seq(
    ws,
    guice,
    "uk.gov.hmrc" %% "government-gateway-domain" % "2.7.0",
    "uk.gov.hmrc" %% "bootstrap-play-26" % "1.3.0"
  )

  val test = Seq(
    "uk.gov.hmrc" %% "government-gateway-test" % "2.6.0" % "test,it"
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
