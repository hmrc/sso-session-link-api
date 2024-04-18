import sbt.*

object AppDependencies {
  private val compile = Seq(
    "uk.gov.hmrc" %% "government-gateway-domain-play-30" % "9.0.0"
  )

  private val test = Seq(
    "org.mockito" %% "mockito-scala-scalatest"         % "1.17.31" % Test,
    "uk.gov.hmrc" %% "bootstrap-test-play-30"          % "8.5.0"   % Test,
    "uk.gov.hmrc" %% "government-gateway-test-play-30" % "6.0.0"   % Test
      excludeAll (
      ExclusionRule("com.fasterxml.jackson.core", "jackson-databind"),
      ExclusionRule("com.fasterxml.jackson.core", "jackson-core"),
      ExclusionRule("com.fasterxml.jackson.core", "jackson-annotations"),
      ExclusionRule("com.fasterxml.jackson.dataformat", "jackson-dataformat-yaml"),
      ExclusionRule("com.fasterxml.jackson.datatype", "jackson-datatype-jsr310")
    )
  )

  def apply(): Seq[ModuleID] = compile ++ test
}
