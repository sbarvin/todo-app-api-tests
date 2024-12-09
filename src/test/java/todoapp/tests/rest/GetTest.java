package todoapp.tests.rest;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import todoapp.models.Todo;
import todoapp.steps.TodoSteps;
import todoapp.tests.TestBase;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static todoapp.enums.HttpStatus.BAD_REQUEST;
import static todoapp.enums.HttpStatus.OK;

@Epic("todo-app")
@Feature("/todos GET")
@Tag("rest")
public class GetTest extends TestBase {

    /*
        TODO GET. Test cases for future version
        1. Send request with zero offset.
        2. Send request with negative offset.
        3. Send request with zero limit.
        4. Send request with large offset.
        5. Send request with limit that equals total TODOs.
        6. Send request with offset that greater than total TODOs.
        ...
     */

    private Todo newTodo;

    @BeforeEach
    void setUp() {
        newTodo = TodoSteps.createNewTodo();
    }

    @AfterEach
    void tearDown() {
        restClient.todo().delete(newTodo.getId());
    }

    @Test
    @Story("positive")
    @DisplayName("Get list of TODOs that isn't empty")
    void getTest() {
        //given
        //when
        var todos = TodoSteps.getAllTodos();

        //then
        assertThat("List of TODOs is empty", todos, not(hasSize(0)));
    }

    @Test
    @Story("positive")
    @DisplayName("Get list of TODOs with valid limit and offset")
    void getWithLimitAndOffsetTest() {
        //given
        var startTodoSize = TodoSteps.getAllTodos().size();

        //when
        var todos = Arrays.stream(restClient.todo().get(startTodoSize - 1, 1)
                .then()
                .statusCode(OK.getValue())
                .extract().as(Todo[].class)).toList();

        //then
        TodoSteps.checkItem(todos, newTodo);
    }

    @Test
    @Story("negative")
    @DisplayName("Get list of TODOs with negative limit")
    void getWithNegativeLimitTest() {
        //given
        //when
        restClient.todo().getWithLimit(-1)
                .then()
                .statusCode(BAD_REQUEST.getValue())
                .body(Matchers.anything());

        //then
    }
}
