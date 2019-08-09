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
import uk.gov.hmrc.gg.config.GenericAppConfig
import uk.gov.hmrc.http.hooks.HttpHooks
import uk.gov.hmrc.http.{HttpDelete, HttpGet, HttpPost, HttpPut}
import uk.gov.hmrc.play.audit.http.HttpAuditing
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.config.{AppName, RunMode}
import uk.gov.hmrc.play.http.ws._
import uk.gov.hmrc.play.microservice.config.LoadAuditingConfig

trait WSHttp extends HttpGet with WSGet with HttpPut with WSPut with HttpPost with WSPost with HttpDelete with WSDelete with AppName with Hooks with GenericAppConfig
object WSHttp extends WSHttp {
  override protected def configuration: Option[Config] = Some(runModeConfiguration.underlying)
}

object MicroserviceAuditConnector extends AuditConnector with RunMode with GenericAppConfig {
  override lazy val auditingConfig = LoadAuditingConfig(s"$env.auditing")
}

trait Hooks extends HttpHooks with HttpAuditing {
  override val hooks = Seq(AuditingHook)
  override lazy val auditConnector: AuditConnector = MicroserviceAuditConnector
}

class SsoAuditConnector extends AuditConnector with GenericAppConfig {
  override lazy val auditingConfig = LoadAuditingConfig(s"$env.auditing")
}
