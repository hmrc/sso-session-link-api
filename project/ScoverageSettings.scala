import scoverage.ScoverageKeys

object ScoverageSettings {
  def apply() = Seq(
    ScoverageKeys.coverageExcludedPackages := Seq(
        "<empty>",
        "Reverse.*",
        ".*?(config|views.*)",
        ".*?(AuthService|BuildInfo|Routes).*",
        ".*?DocumentationController.*",
        ".*?SandboxController.*",
        ".*?GuiceModule.*",
        ".*?microserviceGlobal.*",
        ".*?microserviceWiring"
      ).mkString(";"),
    ScoverageKeys.coverageMinimumStmtTotal := 80,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true
  )
}
