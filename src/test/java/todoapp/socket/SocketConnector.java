package todoapp.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.SneakyThrows;

import java.net.URISyntaxException;
import java.util.Map;

import static todoapp.enums.WsStatus.CLOSE_ABNORMAL;

public class SocketConnector {

    private SocketClient socketClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private SocketConnector() {
    }

    public static SocketConnector ws() {
        return new SocketConnector();
    }

    @Step("Connect to socket with context")
    @SneakyThrows
    public void connect(SocketContext context) {
        Allure.addAttachment("SocketContext", objectMapper.writeValueAsString(context));
        boolean isBodySent = false;
        boolean isRunnableSent = false;
        try {
            socketClient = new SocketClient(context);
            if (!context.getRequestHeaders().isEmpty()) {
                final Map<String, String> requestHeaderParams = context.getRequestHeaders();
                requestHeaderParams.forEach((key, value) ->
                        socketClient.addHeader(key, value));
            }
            socketClient.connectBlocking();
            while (!socketClient.isClosed()) {
                if (socketClient.getAliveTime() >= context.getTimeOut()) {
                    socketClient.close(CLOSE_ABNORMAL.getValue(), "Time Out");
                }
                if (context.getRunnable() != null && !isRunnableSent) {
                    context.getRunnable().run();
                    isRunnableSent = true;
                }
                if (context.getBody() != null && !isBodySent) {
                    socketClient.send(context.getBody());
                    isBodySent = true;
                }
            }
        } catch (URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
