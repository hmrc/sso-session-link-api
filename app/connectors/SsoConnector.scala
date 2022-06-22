/*
 * Copyright 2022 HM Revenue & Customs
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

package connectors

import javax.inject.Inject
import models.{BrowserAffordance, SsoInRequest}
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.http.HttpReads.Implicits._

import scala.concurrent.{ExecutionContext, Future}

class SsoConnector @Inject() (
    http:           HttpClient,
    servicesConfig: ServicesConfig
)(implicit ec: ExecutionContext) {

  private lazy val ssoBaseUrl = servicesConfig.baseUrl("sso")

  def createToken(request: SsoInRequest)(implicit hc: HeaderCarrier): Future[BrowserAffordance] = {
    http.POST[SsoInRequest, BrowserAffordance](s"$ssoBaseUrl/sso/ssoin/sessionInfo", request)
  }
}
