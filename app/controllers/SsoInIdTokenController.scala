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

package controllers

import connectors.SsoConnector
import javax.inject.Inject
import models.SsoInRequest
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Action
import uk.gov.hmrc.http.{BadRequestException, Upstream4xxResponse}
import uk.gov.hmrc.play.microservice.controller.BaseController

import scala.concurrent.ExecutionContext

class SsoInIdTokenController @Inject() (ssoConnector: SsoConnector)(implicit ec: ExecutionContext) extends BaseController {

  def createToken(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    withJsonBody[SsoInRequest] { ssoInRequest =>
      ssoConnector.createToken(ssoInRequest).map(affordance => Created(Json.toJson(affordance)))
    }.recover {
      case e: BadRequestException            => BadRequest(e.getMessage)
      case Upstream4xxResponse(_, 401, _, _) => Unauthorized
      case Upstream4xxResponse(_, 403, _, _) => Forbidden
    }
  }
}
