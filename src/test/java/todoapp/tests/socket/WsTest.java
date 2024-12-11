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
import todoapp.models.SocketMsg;
import todoapp.models.Todo;
import todoapp.socket.SocketConnector;
import todoapp.socket.SocketContext;
import todoapp.tests.TestBase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
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
        var expectedMessage = SocketMsg.create(Todo.create());

        Runnable runPost = () -> restClient.todo().post(expectedMessage.getData());
        context.setURI(SOCKET_ENDPOINT);
        context.setRunnable(runPost);
        context.setExpectedMessage(objectMapper.writeValueAsString(expectedMessage));

        //when
        SocketConnector.ws().connect(context);

        //then
        assertAll(
                ()->assertThat("Expected message not received", context.getStatusCode(), equalTo(CLOSE_NORMAL.getValue())),
                ()->assertThat("Expected message not equal to received",
                        objectMapper.readValue(context.getReceivedMessageList().getLast(), SocketMsg.class),
                        equalTo(expectedMessage)
                )
        );
        restClient.todo().delete(expectedMessage.getData().getId());
    }


}
