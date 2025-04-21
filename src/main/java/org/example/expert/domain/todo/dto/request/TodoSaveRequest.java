package org.example.expert.domain.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TodoSaveRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String contents;

    public TodoSaveRequest(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

}
