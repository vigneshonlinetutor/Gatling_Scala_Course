package simulations.injection

import io.gatling.core.Predef.*
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef.*
import io.gatling.http.protocol.HttpProtocolBuilder

class ThrorttleTest extends Simulation {

  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://reqres.in")

  val scn: ScenarioBuilder = scenario("Get Single User Scenario")
    .exec(
      http("GET API for fetching Single User")
        .get("/api/users/2")
    )

  setUp(scn.inject(
    constantConcurrentUsers(20).during(20)
  ))
    .throttle(
      reachRps(100).in(10),
      holdFor(3),
      jumpToRps(50),
      holdFor(3)
    )
    .protocols(httpProtocol)
}
