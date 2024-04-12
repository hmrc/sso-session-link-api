/*
 * Copyright 2024 HM Revenue & Customs
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

package controllers

import connectors.SsoConnector
import models.SsoInRequest
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, ControllerComponents}
import uk.gov.hmrc.http.UpstreamErrorResponse
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class SsoInIdTokenController @Inject() (ssoConnector: SsoConnector, cc: ControllerComponents)(implicit ec: ExecutionContext)
    extends BackendController(cc) {

  def createToken(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    withJsonBody[SsoInRequest](ssoInRequest => ssoConnector.createToken(ssoInRequest).map(affordance => Created(Json.toJson(affordance))))
      .recover {
        case UpstreamErrorResponse(message, 400, _, _) => BadRequest(message)
        case UpstreamErrorResponse(_, 401, _, _)       => Unauthorized
        case UpstreamErrorResponse(_, 403, _, _)       => Forbidden
      }
  }
}
