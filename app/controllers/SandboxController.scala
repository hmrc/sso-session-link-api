/*
 * Copyright 2020 HM Revenue & Customs
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

import com.google.inject.Inject
import models.SsoInRequest
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, ControllerComponents}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import scala.concurrent.Future

class SandboxController @Inject() (cc: ControllerComponents) extends BackendController(cc) {

  def createToken(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    withJsonBody[SsoInRequest] { _ =>
      Future.successful {
        Ok(Json.obj("_links" -> Json.obj(
          "browser" -> Json.obj("href" -> "/sso/ssoin/5a003c6352000072009a711e")
        )))
      }
    }
  }
}
