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
    "return 500" in new Setup {
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

      result.status shouldBe INTERNAL_SERVER_ERROR
    }
  }

  trait Setup {
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
