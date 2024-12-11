package todoapp.models;

@lombok.Data
public class SocketMsg {

    private Todo data;
    private String type;

    public static SocketMsg create(Todo todo) {
        var socketMsg = new SocketMsg();
        socketMsg.setData(todo);
        socketMsg.setType("new_todo");
        return socketMsg;
    }
}
