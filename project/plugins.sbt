

resolvers += Resolver.url("HMRC Sbt Plugin Releases", url("https://dl.bintray.com/hmrc/sbt-plugin-releases"))(Resolver.ivyStylePatterns)
resolvers += "hmrc-releases" at "https://artefacts.tax.service.gov.uk/artifactory/hmrc-releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.7.6")

addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.3.1")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.12")

addSbtPlugin("uk.gov.hmrc" % "sbt-distributables" % "2.1.0")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.6.1")

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.3")

addSbtPlugin("uk.gov.hmrc" % "sbt-auto-build" % "2.13.0")

addSbtPlugin("uk.gov.hmrc" % "sbt-git-versioning" % "2.2.0")

addSbtPlugin("uk.gov.hmrc" % "sbt-artifactory" % "1.13.0")

addSbtPlugin("uk.gov.hmrc" % "sbt-settings" % "4.7.0")
