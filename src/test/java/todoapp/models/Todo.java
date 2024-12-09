package todoapp.models;

import com.github.javafaker.Faker;
import lombok.Data;

@Data
public class Todo {

    private Boolean completed;
    private Long id;
    private String text;

    private static final Faker faker = new Faker();
    public static Todo create() {
        var todo = new Todo();
        todo.setId(faker.number().randomNumber());
        todo.setText(faker.harryPotter().quote());
        todo.setCompleted(faker.bool().bool());
        return todo;
    }
}
