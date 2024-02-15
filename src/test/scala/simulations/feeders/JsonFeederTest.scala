package simulations.feeders

import io.gatling.core.Predef.*
import io.gatling.core.feeder.Feeder
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef.*
import io.gatling.http.protocol.HttpProtocolBuilder

class JsonFeederTest extends Simulation {

  val token: String = "Bearer b852c5fd133aa4a95e3e5cc40f257dcd2303cd7a8a25211b7e1da9f9d44c684a"

  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://gorest.co.in/public/v2")

  val jsonFeeder: Feeder[Any] = jsonFile("data/feeder/studentDetails.json").circular()

  def getSingleStudentDetail(): ChainBuilder = {
    repeat(3) {
      feed(jsonFeeder)
        .exec(
          http("get single student details of #{id} of the user name #{name}")
            .get("/users/#{id}")
            .header("Authorization", token)
            .check(
              status.is(200),
              jsonPath("$.id").is("#{id}"),
              jsonPath("$.name").is("#{name}")
            )
        )
    }
  }

  val scn: ScenarioBuilder = scenario("JSON Feeder Test")
    .exec(getSingleStudentDetail())

  setUp(scn.inject(atOnceUsers(1)))
    .protocols(httpProtocol)

}
