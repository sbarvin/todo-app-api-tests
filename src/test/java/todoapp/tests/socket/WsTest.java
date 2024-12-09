package todoapp.tests.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import todoapp.config.App;
import todoapp.models.Data;
import todoapp.models.Todo;
import todoapp.socket.SocketConnector;
import todoapp.socket.SocketContext;
import todoapp.tests.TestBase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static todoapp.enums.WsStatus.CLOSE_NORMAL;

@Epic("todo-app")
@Feature("/WS")
@Tag("socket")
public class WsTest extends TestBase {

    /*
        TODO Websocket. Test cases for future version
        1. Receiving multiple updates.
        2. Reconnect.
        3. Check that client receive updates only about new TODOs.
        ...
     */

    private SocketContext context;
    private final String SOCKET_ENDPOINT = App.config.wsUrl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void prepare() {
        context = new SocketContext();
    }

    @Test
    @Story("success")
    @SneakyThrows
    @DisplayName("Get updates about new TODOs")
    void successTest() {
        //given
        var newTodo = Todo.create();
        var expectedMessage = objectMapper.writeValueAsString(Data.create(newTodo));

        Runnable runPost = () -> restClient.todo().post(newTodo);
        context.setURI(SOCKET_ENDPOINT);
        context.setRunnable(runPost);
        context.setExpectedMessage(expectedMessage);

        //when
        SocketConnector.ws().connect(context);

        //then
        assertThat("Expected message not received", context.getStatusCode(), equalTo(CLOSE_NORMAL.getValue()));
        restClient.todo().delete(newTodo.getId());
    }


}
