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

package uk.gov.hmrc.api.services
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime
import play.api.libs.json.Json
import play.api.libs.ws.JsonBodyWritables.writeableOf_JsValue
import uk.gov.hmrc.api.conf.TestEnvironment
import uk.gov.hmrc.apitestrunner.http.HttpClient

import scala.concurrent.Await

class AuthService() extends HttpClient {

  val host: String = TestEnvironment.url("auth-login-api")

  val standardBearerPayload: String =
    """
      |{
      |  "clientId": "id-123232",
      |  "authProvider": "StandardApplication",
      |  "applicationId":"app-1",
      |  "applicationName": "ILMS",
      |  "enrolments": ["notFromEnrolmentStore"],
      |  "ttl": 5000
      |}
      |""".stripMargin

  val privBearerPayload: String = """
      |{
      |  "clientId": "id-123232",
      |  "authProvider": "PrivilegedApplication",
      |  "applicationId":"app-1",
      |  "applicationName": "ILMS",
      |  "enrolments": ["notFromEnrolmentStore"],
      |  "ttl": 5000
      |}
      |""".stripMargin

  def getStandardAppBearerToken =
    Await.result(
      mkRequest(s"$host/application/session/login").post(Json.parse(standardBearerPayload)),
      10.seconds
    )
  def getPrivAppBearerToken     =
    Await.result(
      mkRequest(s"$host/application/session/login").post(Json.parse(privBearerPayload)),
      10.seconds
    )
}
