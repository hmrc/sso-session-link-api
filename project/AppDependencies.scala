import sbt._

object AppDependencies {

  import play.sbt.PlayImport._

  val compile = Seq(
    ws,
    guice,
    "uk.gov.hmrc" %% "government-gateway-domain" % "7.2.0-play-28",
    "uk.gov.hmrc" %% "bootstrap-backend-play-28" % "5.24.0"
  )

  val test = Seq(
    "uk.gov.hmrc" %% "government-gateway-test" % "4.9.0" % "test,it"
  )

  def apply() = compile ++ test
}
