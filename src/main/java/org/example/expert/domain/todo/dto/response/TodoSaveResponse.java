package org.example.expert.domain.todo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;

@Getter
@AllArgsConstructor
public class TodoSaveResponse {

    private final Long id;
    private final String title;
    private final String contents;
    private final String weather;
    private final UserResponse user;

    public static TodoSaveResponse fromTodoWithUser(Todo savedTodo, String weather, User user) {
        return new TodoSaveResponse(
                savedTodo.getId(),
                savedTodo.getTitle(),
                savedTodo.getContents(),
                weather,
                new UserResponse(user.getId(), user.getEmail())
        );
    }
}
