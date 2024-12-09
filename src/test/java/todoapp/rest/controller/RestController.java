package todoapp.rest.controller;

import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import todoapp.rest.client.RestConfig;

import static io.restassured.RestAssured.given;

public abstract class RestController {


    protected ResponseSpecBuilder respSpec;
    protected RequestSpecBuilder reqSpec;

    public RestController(RestConfig config) {
        this.reqSpec = config.getRequestSpecBuilder();
        this.respSpec = config.getResponseSpecBuilder();
    }

    protected PreemptiveBasicAuthScheme getBasicAuthScheme(String username, String password) {
        PreemptiveBasicAuthScheme basicAuthScheme = new PreemptiveBasicAuthScheme();
        basicAuthScheme.setUserName(username);
        basicAuthScheme.setPassword(password);

        return basicAuthScheme;
    }

    protected Response post(String url) {
        return given()
                .spec(reqSpec.build())
                .post(url)
                .then()
                .spec(respSpec.build())
                .extract()
                .response();
    }

    protected Response get(String url) {
        return given()
                .spec(reqSpec.build())
                .get(url)
                .then()
                .spec(respSpec.build())
                .extract()
                .response();
    }

    protected Response delete(String url) {
        return given()
                .spec(reqSpec.build())
                .delete(url)
                .then()
                .spec(respSpec.build())
                .extract()
                .response();
    }

    protected Response put(String url) {
        return given()
                .spec(reqSpec.build())
                .put(url)
                .then()
                .spec(respSpec.build())
                .extract()
                .response();
    }
}