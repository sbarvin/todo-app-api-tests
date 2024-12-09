package todoapp.tests.rest;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import todoapp.models.Todo;
import todoapp.steps.TodoSteps;
import todoapp.tests.TestBase;

import static todoapp.enums.HttpStatus.BAD_REQUEST;
import static todoapp.enums.HttpStatus.CREATED;

@Epic("todo-app")
@Feature("/todos POST")
@Tag("rest")
public class PostTest extends TestBase {

    /*
        TODO POST. Test cases for future version
        1. Repeat request.
        2. Send request without id.
        3. Send request without text.
        4. Send request without completed.
        5. Send request with invalid format of id (id > 64-bit identifier).
        6. Send request with large text.
        ...
     */

    @Test
    @Story("positive")
    @DisplayName("Send a post request with valid body")
    void postTest() {
        //given
        var newTodo = Todo.create();
        //when
        restClient.todo().post(newTodo)
                .then()
                .statusCode(CREATED.getValue())
                .body(Matchers.anything());

        //then
        TodoSteps.checkItem(TodoSteps.getTodoById(newTodo.getId()), newTodo);
        restClient.todo().delete(newTodo.getId());
    }

    @Test
    @Story("negative")
    @DisplayName("Send a post request without body")
    void postWithoutBodyTest() {
        //given
        //when
        restClient.todo().post()
                .then()
                .statusCode(BAD_REQUEST.getValue())
                .body(Matchers.anything());

        //then
    }

    @Test
    @Story("negative")
    @DisplayName("Send a post request with negative id")
    void postWithNegativeIdTest() {
        //given
        var newTodo = Todo.create();
        newTodo.setId(-1L);
        //when
        restClient.todo().post(newTodo)
                .then()
                .statusCode(BAD_REQUEST.getValue())
                .body(Matchers.anything());

        //then
    }
}
