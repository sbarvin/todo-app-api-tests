package todoapp.rest.client;

import todoapp.rest.controller.TodoController;

public class RestClient {
    private RestClient() {
    }

    public static RestClient api() {
        return new RestClient();
    }

    public TodoController todo() {
        return new TodoController(RestConfig.config().build());
    }
}
