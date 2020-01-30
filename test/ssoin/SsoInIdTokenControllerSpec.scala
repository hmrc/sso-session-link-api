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

package ssoin

import java.util.UUID

import connectors.SsoConnector
import controllers.SsoInIdTokenController
import models.BrowserAffordance
import org.mockito.scalatest.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.http.{BadRequestException, Upstream4xxResponse}
import uk.gov.hmrc.play.test.UnitSpec

import scala.concurrent.{ExecutionContext, Future}

class SsoInIdTokenControllerSpec
  extends UnitSpec
  with MockitoSugar
  with GuiceOneAppPerSuite {

  "createToken" should {

    "return 400 Bad Request when the JSON payload is invalid" in new Setup {
      val result = controller.createToken()(FakeRequest().withBody(Json.obj()))
      status(result) shouldBe BAD_REQUEST
    }

    "return 400 Bad Request when SSO returns 400 Bad Request" in new Setup {
      when(mockSsoConnector.createToken(any)(any)).thenReturn(Future.failed(new BadRequestException("the request was bad")))

      val result = controller.createToken()(FakeRequest().withBody(Json.obj("id_token" -> "something")))
      status(result) shouldBe BAD_REQUEST
    }

    "return 401 Unauthorized when the ID token is invalid" in new Setup {
      when(mockSsoConnector.createToken(any)(any)).thenReturn(Future.failed(Upstream4xxResponse("", 401, 401)))

      val result = controller.createToken()(FakeRequest().withBody(Json.obj("id_token" -> "invalid")))
      status(result) shouldBe UNAUTHORIZED
    }

    "return 403 Forbidden when the device ID is missing" in new Setup {
      when(mockSsoConnector.createToken(any)(any)).thenReturn(Future.failed(Upstream4xxResponse("", 403, 403)))

      val result = controller.createToken()(FakeRequest().withBody(Json.obj("id_token" -> "something")))
      status(result) shouldBe FORBIDDEN
    }

    "return 403 Forbidden when the session ID is missing" in new Setup {
      when(mockSsoConnector.createToken(any)(any)).thenReturn(Future.failed(Upstream4xxResponse("", 403, 403)))

      val result = controller.createToken()(FakeRequest().withBody(Json.obj("id_token" -> "something")))
      status(result) shouldBe FORBIDDEN
    }

    "return 201 and an SSO in URI when an ID token and portal session ID are provided" in new Setup {
      when(mockSsoConnector.createToken(any)(any)).thenReturn(Future.successful(BrowserAffordance("/sso/something")))

      val result = controller.createToken()(FakeRequest().withBody(Json.obj(
        "id_token" -> "first.second.third",
        "portalSessionId" -> UUID.randomUUID()
      )))
      status(result) shouldBe CREATED
      contentAsJson(result) shouldBe Json.obj(
        "_links" -> Json.obj(
          "browser" -> Json.obj("href" -> "/sso/something")
        )
      )
    }

    "return 201 and an SSO in URI when an ID token and MDTP session ID are provided" in new Setup {
      when(mockSsoConnector.createToken(any)(any)).thenReturn(Future.successful(BrowserAffordance("/sso/something")))

      val result = controller.createToken()(FakeRequest().withBody(Json.obj(
        "id_token" -> "first.second.third",
        "mdtpSessionId" -> UUID.randomUUID()
      )))
      status(result) shouldBe CREATED
      contentAsJson(result) shouldBe Json.obj(
        "_links" -> Json.obj(
          "browser" -> Json.obj("href" -> "/sso/something")
        )
      )
    }

    "return 201 and an SSO in URI when an ID token, MDTP session ID, and a portal session ID are provided" in new Setup {
      when(mockSsoConnector.createToken(any)(any)).thenReturn(Future.successful(BrowserAffordance("/sso/something")))

      val result = controller.createToken()(FakeRequest().withBody(Json.obj(
        "id_token" -> "first.second.third",
        "mdtpSessionId" -> UUID.randomUUID(),
        "portalSessionId" -> UUID.randomUUID()
      )))
      status(result) shouldBe CREATED
      contentAsJson(result) shouldBe Json.obj(
        "_links" -> Json.obj(
          "browser" -> Json.obj("href" -> "/sso/something")
        )
      )
    }
  }

  trait Setup {
    val mockSsoConnector = mock[SsoConnector]
    val controller = new SsoInIdTokenController(mockSsoConnector)(ExecutionContext.global)
  }
}
