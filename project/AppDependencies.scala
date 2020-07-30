import sbt._

object AppDependencies {

  import play.sbt.PlayImport._

  val compile = Seq(
    ws,
    guice,
    "uk.gov.hmrc" %% "government-gateway-domain" % "5.2.0",
    "uk.gov.hmrc" %% "bootstrap-play-26" % "1.14.0"
  )

  val test = Seq(
    "uk.gov.hmrc" %% "government-gateway-test" % "3.2.0" % "test,it"
  )

  def apply() = compile ++ test
}
