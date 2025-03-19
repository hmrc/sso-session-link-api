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

import java.util.UUID
import play.api.libs.json.Json
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.matching.AnythingPattern
import play.api.libs.ws.WSResponse
import play.api.libs.ws.writeableOf_JsValue
import support.WireMockSpec
import uk.gov.hmrc.http.HeaderNames

class SsoInIdTokenControllerWireMockSpec extends WireMockSpec {

  "POST /ssoin/sessionInfo" should {
    "return 400 Bad Request when SSO returns 400 Bad Request" in new Setup {
      expectSsoToReturnBadRequest()

      val result: WSResponse = await(
        resourceRequest("/ssoin/sessionInfo")
          .withHttpHeaders(HeaderNames.xSessionId -> UUID.randomUUID().toString)
          .addHttpHeaders(HeaderNames.deviceID -> UUID.randomUUID().toString)
          .post(
            Json.obj(
              "id_token" -> "something"
            )
          )
      )

      result.status shouldBe BAD_REQUEST
    }

    "return 401 Unauthorized when the ID token is invalid" in new Setup {
      expectSsoToReturnUnauthorizedForInvalidToken()

      val result: WSResponse = await(
        resourceRequest("/ssoin/sessionInfo")
          .withHttpHeaders(HeaderNames.xSessionId -> UUID.randomUUID().toString)
          .addHttpHeaders(HeaderNames.deviceID -> UUID.randomUUID().toString)
          .post(
            Json.obj(
              "id_token" -> "invalid"
            )
          )
      )

      result.status shouldBe UNAUTHORIZED
    }

    "return 403 Forbidden when the device ID is missing" in new Setup {
      expectSsoToReturnForbiddenForMissingDeviceId()

      val result: WSResponse = await(
        resourceRequest("/ssoin/sessionInfo")
          .withHttpHeaders(HeaderNames.xSessionId -> UUID.randomUUID().toString)
          .post(
            Json.obj(
              "id_token" -> "first.second.third"
            )
          )
      )

      result.status shouldBe FORBIDDEN
    }

    "return 403 Forbidden when the session ID is missing" in new Setup {
      expectSsoToReturnForbiddenForMissingSessionId()

      val result: WSResponse = await(
        resourceRequest("/ssoin/sessionInfo")
          .withHttpHeaders(HeaderNames.deviceID -> UUID.randomUUID().toString)
          .post(
            Json.obj(
              "id_token" -> "first.second.third"
            )
          )
      )

      result.status shouldBe FORBIDDEN
    }

    "return 201 and an SSO in URI when the request is valid" in new Setup {
      expectSsoToReturnCreatedForValidRequest()

      val result: WSResponse = await(
        resourceRequest("/ssoin/sessionInfo")
          .withHttpHeaders(HeaderNames.xSessionId -> UUID.randomUUID().toString)
          .addHttpHeaders(HeaderNames.deviceID -> UUID.randomUUID().toString)
          .post(
            Json.obj(
              "id_token" -> "first.second.third"
            )
          )
      )

      result.status shouldBe CREATED
      result.json shouldBe Json.obj(
        "_links" -> Json.obj(
          "browser" -> Json.obj("href" -> "/sso/ssoin/5a003c6352000072009a711e")
        )
      )
    }
  }

  trait Setup {
    def expectSsoToReturnBadRequest(): Unit =
      stubFor(
        post("/sso/ssoin/sessionInfo")
          .withRequestBody(
            equalToJson(
              Json.obj("id_token" -> "something").toString
            )
          )
          .withHeader(HeaderNames.deviceID, new AnythingPattern())
          .withHeader(HeaderNames.xSessionId, new AnythingPattern())
          .willReturn(badRequest())
      )

    def expectSsoToReturnUnauthorizedForInvalidToken(): Unit =
      stubFor(
        post("/sso/ssoin/sessionInfo")
          .withRequestBody(
            equalToJson(
              Json.obj("id_token" -> "invalid").toString
            )
          )
          .withHeader(HeaderNames.deviceID, new AnythingPattern())
          .withHeader(HeaderNames.xSessionId, new AnythingPattern())
          .willReturn(unauthorized())
      )

    def expectSsoToReturnForbiddenForMissingDeviceId(): Unit =
      stubFor(
        post("/sso/ssoin/sessionInfo")
          .withRequestBody(
            equalToJson(
              Json.obj("id_token" -> "first.second.third").toString
            )
          )
          .withHeader(HeaderNames.deviceID, absent())
          .withHeader(HeaderNames.xSessionId, new AnythingPattern())
          .willReturn(forbidden())
      )

    def expectSsoToReturnForbiddenForMissingSessionId(): Unit =
      stubFor(
        post("/sso/ssoin/sessionInfo")
          .withRequestBody(
            equalToJson(
              Json.obj("id_token" -> "first.second.third").toString
            )
          )
          .withHeader(HeaderNames.deviceID, new AnythingPattern())
          .withHeader(HeaderNames.xSessionId, absent())
          .willReturn(forbidden())
      )

    def expectSsoToReturnCreatedForValidRequest(): Unit =
      stubFor(
        post("/sso/ssoin/sessionInfo")
          .withRequestBody(
            equalToJson(
              Json.obj("id_token" -> "first.second.third").toString
            )
          )
          .withHeader(HeaderNames.deviceID, new AnythingPattern())
          .withHeader(HeaderNames.xSessionId, new AnythingPattern())
          .willReturn(
            created().withBody(
              Json
                .obj(
                  "_links" -> Json.obj(
                    "browser" -> Json.obj("href" -> "/sso/ssoin/5a003c6352000072009a711e")
                  )
                )
                .toString
            )
          )
      )
  }

}
