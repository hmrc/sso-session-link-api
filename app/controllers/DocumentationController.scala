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

import javax.inject.Inject
import play.api.http.MimeTypes
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

class DocumentationController @Inject() (cc: ControllerComponents, assets: Assets) extends BackendController(cc) {
  def definition(): Action[AnyContent] = Action(_ => Ok(views.txt.definition()).withHeaders(CONTENT_TYPE -> MimeTypes.JSON))

  def document(version: String, file: String): Action[AnyContent] =
    assets.at(s"/public/api/conf/$version", file)
}
