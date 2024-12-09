package todoapp.models;

@lombok.Data
public class Data {

    private Todo data;
    private String type;

    public static Data create(Todo todo) {
        var data = new Data();
        data.setData(todo);
        data.setType("new_todo");
        return data;
    }
}
