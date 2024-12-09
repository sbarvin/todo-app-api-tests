package todoapp.rest.client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import lombok.Getter;
import todoapp.config.App;

import static todoapp.rest.client.CustomApiListener.withCustomTemplates;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.http.ContentType.JSON;

@Getter
public class RestConfig {

    private RequestSpecBuilder requestSpecBuilder;
    private ResponseSpecBuilder responseSpecBuilder;

    private RestConfig() {
    }

    public static RestConfig config() {
        return new RestConfig();
    }

    public RestConfig build() {
        this.requestSpecBuilder = new RequestSpecBuilder()
                .setContentType(JSON)
                .setBaseUri(App.config.baseUrl())
                .addFilter(withCustomTemplates())
                .log(ALL);
        this.responseSpecBuilder = new ResponseSpecBuilder().log(ALL);

        return this;
    }
}
