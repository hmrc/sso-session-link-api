import sbt.*

object AppDependencies {
  private val bootstrapVersion = "9.9.0"

  private val compile = Seq(
    "uk.gov.hmrc" %%       "bootstrap-common-play-30"    % bootstrapVersion,
    "uk.gov.hmrc" %% "government-gateway-domain-play-30" % "10.1.0"
  )

  private val test = Seq(
    "org.scalatestplus" %% "mockito-4-11"                    % "3.2.17.0"       % Test,
    "uk.gov.hmrc"       %% "bootstrap-test-play-30"          % bootstrapVersion % Test,
    "uk.gov.hmrc"       %% "government-gateway-test-play-30" % "7.1.0"          % Test
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
