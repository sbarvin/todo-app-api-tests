package todoapp.tests.rest;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import todoapp.steps.TodoSteps;
import todoapp.tests.TestBase;

import static todoapp.enums.HttpStatus.NOT_FOUND;
import static todoapp.enums.HttpStatus.NO_CONTENT;
import static todoapp.enums.HttpStatus.UNAUTHORIZED;

@Epic("todo-app")
@Feature("/todos/:id DELETE")
@Tag("rest")
public class DeleteTest extends TestBase {

    /*
        TODO DELETE. Test cases for future version
        1. Delete non-existing item.
        2. Send request without id.
        3. Send request with invalid format of id (id > 64-bit identifier).
        ...
     */

    @Test
    @Story("positive")
    @DisplayName("Delete a TODO")
    void deleteTest() {
        //given
        var newTodo = TodoSteps.createNewTodo();
        //when
        restClient.todo().delete(newTodo.getId())
                .then()
                .statusCode(NO_CONTENT.getValue())
                .body(Matchers.anything());

        //then
        TodoSteps.checkItemWithId(newTodo.getId(), 0);
    }

    @Test
    @Story("negative")
    @DisplayName("Delete a TODO without auth")
    void deleteWithoutAuthTest() {
        //given
        var newTodo = TodoSteps.createNewTodo();
        //when
        restClient.todo().deleteWithoutAuth(newTodo.getId())
                .then()
                .statusCode(UNAUTHORIZED.getValue())
                .body(Matchers.anything());

        //then
        TodoSteps.checkItemWithId(newTodo.getId(), 1);
        restClient.todo().delete(newTodo.getId());
    }

    @Test
    @Story("negative")
    @DisplayName("Delete TODO with negative id")
    void deleteTodoWithNegativeIdTest() {
        //given
        //when
        restClient.todo().delete(-1L)
                .then()
                .statusCode(NOT_FOUND.getValue())
                .body(Matchers.anything());

        //then
    }
}
