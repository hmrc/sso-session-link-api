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

    "return 400 Bad Request when the JSON payload is invalid" in new Setup {
      val result: Future[Result] = controller.createToken()(FakeRequest().withBody(Json.obj()))
      status(result) shouldBe BAD_REQUEST
    }

    "return 400 Bad Request when SSO returns 400 Bad Request" in new Setup {
      when(mockSsoConnector.createToken(any)(any)).thenReturn(Future.failed(UpstreamErrorResponse("the request was bad", 400, 400)))

      val result: Future[Result] = controller.createToken()(FakeRequest().withBody(Json.obj("id_token" -> "something")))
      status(result) shouldBe BAD_REQUEST
    }

    "return 401 Unauthorized when the ID token is invalid" in new Setup {
      when(mockSsoConnector.createToken(any)(any)).thenReturn(Future.failed(UpstreamErrorResponse("", 401, 401)))

      val result: Future[Result] = controller.createToken()(FakeRequest().withBody(Json.obj("id_token" -> "invalid")))
      status(result) shouldBe UNAUTHORIZED
    }

    "return 403 Forbidden when the device ID is missing" in new Setup {
      when(mockSsoConnector.createToken(any)(any)).thenReturn(Future.failed(UpstreamErrorResponse("", 403, 403)))

      val result: Future[Result] = controller.createToken()(FakeRequest().withBody(Json.obj("id_token" -> "something")))
      status(result) shouldBe FORBIDDEN
    }

    "return 403 Forbidden when the session ID is missing" in new Setup {
      when(mockSsoConnector.createToken(any)(any)).thenReturn(Future.failed(UpstreamErrorResponse("", 403, 403)))

      val result: Future[Result] = controller.createToken()(FakeRequest().withBody(Json.obj("id_token" -> "something")))
      status(result) shouldBe FORBIDDEN
    }

    "return 201 and an SSO in URI when an ID token and portal session ID are provided" in new Setup {
      when(mockSsoConnector.createToken(any)(any)).thenReturn(Future.successful(BrowserAffordance("/sso/something")))

      val result: Future[Result] = controller.createToken()(
        FakeRequest().withBody(
          Json.obj(
            "id_token"        -> "first.second.third",
            "portalSessionId" -> UUID.randomUUID()
          )
        )
      )
      status(result) shouldBe CREATED
      contentAsJson(result) shouldBe Json.obj(
        "_links" -> Json.obj(
          "browser" -> Json.obj("href" -> "/sso/something")
        )
      )
    }

    "return 201 and an SSO in URI when an ID token and MDTP session ID are provided" in new Setup {
      when(mockSsoConnector.createToken(any)(any)).thenReturn(Future.successful(BrowserAffordance("/sso/something")))

      val result: Future[Result] = controller.createToken()(
        FakeRequest().withBody(
          Json.obj(
            "id_token"      -> "first.second.third",
            "mdtpSessionId" -> UUID.randomUUID()
          )
        )
      )
      status(result) shouldBe CREATED
      contentAsJson(result) shouldBe Json.obj(
        "_links" -> Json.obj(
          "browser" -> Json.obj("href" -> "/sso/something")
        )
      )
    }

    "return 201 and an SSO in URI when an ID token, MDTP session ID, and a portal session ID are provided" in new Setup {
      when(mockSsoConnector.createToken(any)(any)).thenReturn(Future.successful(BrowserAffordance("/sso/something")))

      val result: Future[Result] = controller.createToken()(
        FakeRequest().withBody(
          Json.obj(
            "id_token"        -> "first.second.third",
            "mdtpSessionId"   -> UUID.randomUUID(),
            "portalSessionId" -> UUID.randomUUID()
          )
        )
      )
      status(result) shouldBe CREATED
      contentAsJson(result) shouldBe Json.obj(
        "_links" -> Json.obj(
          "browser" -> Json.obj("href" -> "/sso/something")
        )
      )
    }
  }

  trait Setup {
    val mockSsoConnector: SsoConnector = mock[SsoConnector]
    val cc: ControllerComponents = app.injector.instanceOf(classOf[ControllerComponents])
    val controller = new SsoInIdTokenController(mockSsoConnector, cc)(ExecutionContext.global)
  }
}
