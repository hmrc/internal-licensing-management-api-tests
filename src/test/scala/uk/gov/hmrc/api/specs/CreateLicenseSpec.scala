/*
 * Copyright 2025 HM Revenue & Customs
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

package uk.gov.hmrc.api.specs

import play.api.libs.ws.JsonBodyWritables.writeableOf_JsValue
import uk.gov.hmrc.api.conf.TestEnvironment
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime
import play.api.libs.json.Json

import scala.concurrent.Await

class CreateLicenseSpec extends BaseSpec {
  val createLicenseUrl  = TestEnvironment.url("internal-licensing-management-api")
  val errorStubRef      = "GBOIL123456"
  val successfulStubRef = "GBOIL123457"

  Feature("Create Licenses") {
    Scenario("The service receives standardApp auth token and returns 403") {
      When("create license is called with the following payload elements")
      val bearer = authHelper.getStandardAppToken
      val result = Await.result(
        mkRequest(s"$createLicenseUrl/$successfulStubRef")
          .addHttpHeaders("authorization" -> bearer)
          .put(Json.parse("{}")),
        3.seconds
      )
      Then("response code should be 403")
      result.status shouldBe 403
    }

    Scenario("The service receives priv app auth token and returns 200") {
      When("create license is called with the following payload elements")
      val bearer = authHelper.getPrivAppToken
      val result = Await.result(
        mkRequest(s"$createLicenseUrl/$successfulStubRef")
          .addHttpHeaders("authorization" -> bearer)
          .put(Json.parse("{}")),
        3.seconds
      )
      Then("response code should be 200")
      result.status shouldBe 200
    }
    Scenario("The service receives priv app auth token and returns 400 for error") {
      When("create license is called with the following payload elements")
      val bearer = authHelper.getPrivAppToken
      val result = Await.result(
        mkRequest(s"$createLicenseUrl/$errorStubRef")
          .addHttpHeaders("authorization" -> bearer)
          .put(Json.parse("{}")),
        3.seconds
      )
      Then("response code should be 400")
      result.status shouldBe 400
    }
  }
}
