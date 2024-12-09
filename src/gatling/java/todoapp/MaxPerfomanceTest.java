package todoapp;

import com.github.javafaker.Faker;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpDsl;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import todoapp.config.App;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

public class MaxPerfomanceTest extends Simulation {

    private Faker faker = new Faker();
    private ArrayList<Long> createdIds = new ArrayList<>();
    HttpProtocolBuilder httpProtocol =
            HttpDsl.http.baseUrl(App.config.baseUrl())
                    .acceptHeader("application/json")
                    .contentTypeHeader("application/json");


    Iterator<Map<String, Object>> feeder =
            Stream.generate((Supplier<Map<String, Object>>) () -> {
                        var id = faker.number().randomNumber();
                        while (createdIds.contains(id)) {
                            id = faker.number().randomNumber();
                        }
                        createdIds.add(id);
                        return Map.of(
                                "text", faker.harryPotter().quote(),
                                "id", id,
                                "completed", faker.bool().bool()
                        );
                    }
            ).iterator();

    ScenarioBuilder postScn = CoreDsl.scenario("post /todos/")
            .feed(feeder)
            .exec(HttpDsl.http("Create new TODO /todos/")
                    .post("/todos/")
                    .body(CoreDsl.ElFileBody("data/todo.json")).asJson()
                    .check(HttpDsl.status().is(201)));

    {
        setUp(
                postScn.injectOpen(
                        //интенсивность на ступень
                        CoreDsl.incrementUsersPerSec(1)
                                // кол-во ступеней
                                .times(3)
                                // длительность полки
                                .eachLevelLasting(2)
                                // длительность разгона
                                .separatedByRampsLasting(1)
                                // начало нагрузки с
                                .startingFrom(0)
                )
        ).protocols(httpProtocol);
    }

    @Override
    public void after() {
        createdIds.forEach(
                id -> given().baseUri(App.config.baseUrl())
                        .auth().preemptive().basic(App.config.username(), App.config.password())
                        .delete("/todos/" + id)
        );
    }
}
