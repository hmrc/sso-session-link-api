import scoverage.ScoverageKeys

object ScoverageSettings {
  def apply() = Seq(
    ScoverageKeys.coverageExcludedPackages :=
      """<empty>;
        |Reverse.*;
        |.*?(config|views.*);
        |.*?(AuthService|BuildInfo|Routes).*;
        |.*?DocumentationController.*;
        |.*?SandboxController.*;
        |.*?GuiceModule.*;
        |.*?microserviceGlobal.*;
        |.*?microserviceWiring
        |""".stripMargin,
    ScoverageKeys.coverageMinimum := 80,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true
  )
}
