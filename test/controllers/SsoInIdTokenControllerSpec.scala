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
import models.BrowserAffordance
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.Json
import play.api.mvc.{ControllerComponents, Result}
import play.api.test.FakeRequest
import support.UnitSpec
import uk.gov.hmrc.http.UpstreamErrorResponse

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class SsoInIdTokenControllerSpec extends UnitSpec with GuiceOneAppPerSuite {

  "createToken" should {

    "return 500 when an ID token and portal session ID are provided" in new Setup {
      when(mockSsoConnector.createToken(any)(any)).thenReturn(Future.successful(BrowserAffordance("/sso/something")))

      val result: Future[Result] = controller.createToken()(
        FakeRequest().withBody(
          Json.obj(
            "id_token"        -> "first.second.third",
            "portalSessionId" -> UUID.randomUUID()
          )
        )
      )
      status(result) shouldBe INTERNAL_SERVER_ERROR
    }
  }

  trait Setup {
    val mockSsoConnector: SsoConnector = mock[SsoConnector]
    val cc: ControllerComponents = app.injector.instanceOf(classOf[ControllerComponents])
    val controller = new SsoInIdTokenController(mockSsoConnector, cc)(ExecutionContext.global)
  }
}
