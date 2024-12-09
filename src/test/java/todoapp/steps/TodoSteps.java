package todoapp.steps;

import io.qameta.allure.Step;
import org.hamcrest.Matchers;
import todoapp.models.Todo;
import todoapp.rest.client.RestClient;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToObject;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;
import static todoapp.enums.HttpStatus.CREATED;
import static todoapp.enums.HttpStatus.OK;

public class TodoSteps {

    public static final RestClient restClient = RestClient.api();

    public static List<Todo> getAllTodos() {
        return Arrays.stream(restClient.todo().get()
                .then()
                .statusCode(OK.getValue())
                .extract().as(Todo[].class)).toList();
    }

    public static List<Todo> getTodoById(Long id) {
        return Arrays.stream(restClient.todo().get()
                        .then()
                        .statusCode(OK.getValue())
                        .extract().as(Todo[].class))
                .filter(t -> t.getId().equals(id))
                .toList();
    }

    public static Todo createNewTodo() {
        var newTodo = Todo.create();
        restClient.todo().post(newTodo)
                .then()
                .statusCode(CREATED.getValue())
                .body(Matchers.anything());
        return newTodo;
    }

    @Step("Check that the quantity of TODOs with id = {expectedId} in the list is {expectedSize}")
    public static void checkItemWithId(Long expectedId, int expectedSize) {
        assertThat("The quantity of TODOs with id = " + expectedId + " in the list != " + expectedSize,
                getTodoById(expectedId),
                hasSize(expectedSize)
        );
    }

    @Step("Check that actual TODO equal to expected")
    public static void checkItem(List<Todo> todos, Todo expectedTodo) {
        assertAll(
                () -> assertThat("The quantity of TODOs with id = " + expectedTodo.getId() + " in the list != 1",
                        todos,
                        hasSize(1)
                ),
                () -> assertThat("The actual TODO isn't equal to expected",
                        todos.getFirst(),
                        equalToObject(expectedTodo)
                )
        );
    }
}
