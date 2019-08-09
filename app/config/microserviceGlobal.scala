/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package config

import com.typesafe.config.Config
import net.ceedubs.ficus.Ficus._
import play.api._
import play.api.mvc._
import uk.gov.hmrc.api.config.ServiceLocatorConfig
import uk.gov.hmrc.api.connector.ServiceLocatorConnector
import uk.gov.hmrc.gg.config.GenericAppConfig
import uk.gov.hmrc.http.{CorePost, HeaderCarrier}
import uk.gov.hmrc.play.auth.controllers.AuthParamsControllerConfig
import uk.gov.hmrc.play.config.{AppName, ControllerConfig, RunMode, ServicesConfig}
import uk.gov.hmrc.play.microservice.bootstrap.DefaultMicroserviceGlobal
import uk.gov.hmrc.play.microservice.filters.{AuditFilter, LoggingFilter, MicroserviceFilterSupport, NoCacheFilter}

object ControllerConfiguration extends ControllerConfig {
  lazy val controllerConfigs = Play.current.configuration.underlying.as[Config]("controllers")
}

object AuthParamsControllerConfiguration extends AuthParamsControllerConfig {
  lazy val controllerConfigs = ControllerConfiguration.controllerConfigs
}

object MicroserviceAuditFilter extends AuditFilter with AppName with MicroserviceFilterSupport with GenericAppConfig {
  override val auditConnector = MicroserviceAuditConnector

  override def controllerNeedsAuditing(controllerName: String) = ControllerConfiguration.paramsForController(controllerName).needsAuditing
}

object MicroserviceLoggingFilter extends LoggingFilter with MicroserviceFilterSupport {
  override def controllerNeedsLogging(controllerName: String) = ControllerConfiguration.paramsForController(controllerName).needsLogging
}

object MicroserviceGlobal extends DefaultMicroserviceGlobal with ServiceLocatorConnector with ServiceLocatorConfig with RunMode with GenericAppConfig {
  override val auditConnector = MicroserviceAuditConnector

  override def microserviceMetricsConfig(implicit app: Application): Option[Configuration] = app.configuration.getConfig(s"$env.microservice.metrics")

  override val loggingFilter = MicroserviceLoggingFilter
  override val microserviceAuditFilter = MicroserviceAuditFilter
  override val authFilter = None
  lazy val registrationEnabled = Play.current.configuration.getBoolean(s"$env.microservice.services.service-locator.enabled").getOrElse(true)

  override def microserviceFilters: Seq[EssentialFilter] =
    defaultMicroserviceFilters.filterNot(_ == NoCacheFilter) :+ DefaultNoCacheFilter

  implicit val hc: HeaderCarrier = HeaderCarrier()
  override lazy val appUrl: String = getString("appUrl")
  override lazy val serviceUrl: String = serviceLocatorUrl
  override val handlerOK: () => Unit = () => Logger.info("Service is registered on the service locator")
  override val handlerError: Throwable => Unit = e => Logger.error(s"Service could not register on the service locator", e)
  override val metadata: Option[Map[String, String]] = Some(Map("third-party-api" -> "true"))
  override val http: CorePost = WSHttp
}
