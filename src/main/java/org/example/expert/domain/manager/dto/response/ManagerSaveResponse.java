package org.example.expert.domain.manager.dto.response;

import lombok.Getter;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;

@Getter
public class ManagerSaveResponse {

    private final Long id;
    private final UserResponse user;

    public ManagerSaveResponse(Long id, UserResponse user) {
        this.id = id;
        this.user = user;
    }

    public static ManagerSaveResponse from(long id, User user){
        return new ManagerSaveResponse(id,
                new UserResponse(user.getId(),user.getEmail())
                );
    }
}
