import play.sbt.PlayImport._
import sbt._

object AppDependencies {
  private val bootstrapVer = "7.9.0"

  private val compile = Seq(
    ws,
    guice,
    "uk.gov.hmrc" %% "government-gateway-domain" % "8.0.0-play-28",
    "uk.gov.hmrc" %% "bootstrap-backend-play-28" % bootstrapVer
  )

  private val test = Seq(
    "uk.gov.hmrc" %% "government-gateway-test" % "5.0.0" % "test,it"
  )

  def apply(): Seq[ModuleID] = compile ++ test
}
