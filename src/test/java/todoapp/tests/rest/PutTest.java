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

import static io.qameta.allure.Allure.step;
import static todoapp.enums.HttpStatus.NOT_FOUND;
import static todoapp.enums.HttpStatus.OK;

@Epic("todo-app")
@Feature("/todos/:id PUT")
@Tag("rest")
public class PutTest extends TestBase {

    /*
        TODO PUT. Test cases for future version
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
    @DisplayName("Update a TODO")
    void putTest() {
        //given
        var newTodo = TodoSteps.createNewTodo();
        step("Set empty text for TODO with id" + newTodo.getId(),
                () -> newTodo.setText("")
        );
        //when
        restClient.todo().put(newTodo)
                .then()
                .statusCode(OK.getValue())
                .body(Matchers.anything());

        //then
        TodoSteps.checkItem(TodoSteps.getTodoById(newTodo.getId()), newTodo);
        restClient.todo().delete(newTodo.getId());
    }

    @Test
    @Story("negative")
    @DisplayName("Update a non-existent TODO")
    void putNonExistentTodoTest() {
        //given
        var newTodo = Todo.create();
        //when
        restClient.todo().put(newTodo)
                .then()
                .statusCode(NOT_FOUND.getValue())
                .body(Matchers.anything());

        //then
        TodoSteps.checkItemWithId(newTodo.getId(), 0);
    }
}
