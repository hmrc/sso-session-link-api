import sbt._

object AppDependencies {

  import play.sbt.PlayImport._

  val compile = Seq(
    ws,
    guice,
    "uk.gov.hmrc" %% "government-gateway-domain" % "6.1.0-play-27",
    "uk.gov.hmrc" %% "bootstrap-backend-play-27" % "2.25.0"
  )

  val test = Seq(
    "uk.gov.hmrc" %% "government-gateway-test" % "4.2.0-play-27" % "test,it"
  )

  def apply() = compile ++ test
}
