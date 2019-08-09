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

package config

import play.api.mvc.{Filter, RequestHeader, Result}
import play.mvc.Http.HeaderNames
import uk.gov.hmrc.play.microservice.filters.MicroserviceFilterSupport

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object DefaultNoCacheFilter extends Filter with MicroserviceFilterSupport {

  private val NoCache = "no-cache,no-store,max-age=0"

  def apply(next: (RequestHeader) => Future[Result])(rh: RequestHeader): Future[Result] = {
    next(rh).map{ resp =>
      if (resp.header.headers.contains(HeaderNames.CACHE_CONTROL)) resp
      else resp.withHeaders(HeaderNames.CACHE_CONTROL -> NoCache)
    }
  }
}
