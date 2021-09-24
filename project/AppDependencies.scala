import sbt._

object AppDependencies {

  import play.sbt.PlayImport._

  val compile = Seq(
    ws,
    guice,
    "uk.gov.hmrc" %% "government-gateway-domain" % "7.0.0-play-28",
    "uk.gov.hmrc" %% "bootstrap-backend-play-28" % "5.14.0"
  )

  val test = Seq(
    "uk.gov.hmrc" %% "government-gateway-test" % "4.6.0-play-28" % "test,it"
  )

  def apply() = compile ++ test
}
