package utils

import com.typesafe.config.ConfigFactory
import play.api.http.{HeaderNames, MimeTypes, Status}
import uk.gov.hmrc.gg.test.it.SmIntegrationWithWiremockSpecBase

import scala.collection.JavaConverters._

trait WireMockSpec
  extends SmIntegrationWithWiremockSpecBase
    with HeaderNames
    with Status
    with MimeTypes {

  override protected def extraConfig = super.extraConfig ++ sendAllExternalRequestsToWireMockServer()

  private def sendAllExternalRequestsToWireMockServer() = {
    ConfigFactory.load().getConfig("Dev.microservice.services")
      .entrySet()
      .asScala
      .collect {
        case e if e.getKey.endsWith("port") =>
          "Test.microservice.services." + e.getKey -> wiremockPort
      }
      .toMap
  }
}
