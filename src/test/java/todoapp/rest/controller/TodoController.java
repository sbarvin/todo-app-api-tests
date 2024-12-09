package todoapp.rest.controller;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import todoapp.config.App;
import todoapp.models.Todo;
import todoapp.rest.client.RestConfig;

public class TodoController extends RestController {

    private static final String TODOS_ENDPOINT = "/todos";

    public TodoController(RestConfig config) {
        super(config);
    }

    @Step("Get list of TODOs")
    public Response get() {
        return get(TODOS_ENDPOINT);
    }

    @Step("Get list of TODOs with offset = {offset} and limit = {limit}")
    public Response get(long offset, long limit) {
        reqSpec
                .addQueryParam("offset", offset)
                .addQueryParam("limit", limit);
        return get(TODOS_ENDPOINT);
    }

    @Step("Get list of TODOs with limit = {limit}")
    public Response getWithLimit(long limit) {
        reqSpec.addQueryParam("limit", limit);
        return get(TODOS_ENDPOINT);
    }

    @Step("Create TODO")
    public Response post(Todo todo) {
        reqSpec.setBody(todo);
        return post(TODOS_ENDPOINT);
    }

    @Step("Send post request without body")
    public Response post() {
        return post(TODOS_ENDPOINT);
    }

    @Step("Update TODO")
    public Response put(Todo todo) {
        reqSpec.setBody(todo);
        return put(TODOS_ENDPOINT + "/" + todo.getId());
    }

    @Step("Delete TODO")
    public Response delete(Long id) {
        reqSpec.setAuth(getBasicAuthScheme(App.config.username(), App.config.password()));
        return delete(TODOS_ENDPOINT + "/" + id);
    }

    @Step("Delete TODO without auth")
    public Response deleteWithoutAuth(Long id) {
        return delete(TODOS_ENDPOINT + "/" + id);
    }
}
