package org.example.expert.domain.manager.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    @Transactional
    public ManagerSaveResponse saveManager(AuthUser authUser, long todoId, ManagerSaveRequest managerSaveRequest) {
        // 일정을 만든 유저
        User user = User.fromAuthUser(authUser);
        Todo todo = todoRepository.findByIdOrElseThrow(todoId);
        // null.getId() -> NPE
        if (todo.getUser() == null || !ObjectUtils.nullSafeEquals(user.getId(), todo.getUser().getId())) {
            throw new InvalidRequestException("담당자를 등록하려고 하는 유저가 일정을 만든 유저가 유효하지 않습니다.");
        }

        User managerUser = userRepository.findById(managerSaveRequest.getManagerUserId())
                .orElseThrow(() -> new InvalidRequestException("등록하려고 하는 담당자 유저가 존재하지 않습니다."));

        if (ObjectUtils.nullSafeEquals(user.getId(), managerUser.getId())) {
            throw new InvalidRequestException("일정 작성자는 본인을 담당자로 등록할 수 없습니다.");
        }

        Manager newManagerUser = Manager.of(managerUser, todo);
        Manager savedManagerUser = managerRepository.save(newManagerUser);

        return ManagerSaveResponse.from(
                savedManagerUser.getId(),
                managerUser
        );
    }

    @Transactional(readOnly = true)
    public List<ManagerResponse> getManagers(long todoId) {
        Todo todo = todoRepository.findByIdOrElseThrow(todoId);

        List<Manager> managerList = managerRepository.findByTodoIdWithUser(todo.getId());

        return managerList.stream()
                .map(ManagerResponse::fromManagerWithUser)
                .toList();

    }

    @Transactional
    public void deleteManager(long userId, long todoId, long managerId) {
        User user = userRepository.findByIdOrElseThrow(userId);

        Todo todo = todoRepository.findByIdOrElseThrow(todoId);

        if (todo.getUser() == null || !ObjectUtils.nullSafeEquals(user.getId(), todo.getUser().getId())) {
            throw new InvalidRequestException("해당 일정을 만든 유저가 유효하지 않습니다.");
        }

        Manager manager = managerRepository.findByIdOrElseThrow(managerId);

        if (!ObjectUtils.nullSafeEquals(todo.getId(), manager.getTodo().getId())) {
            throw new InvalidRequestException("해당 일정에 등록된 담당자가 아닙니다.");
        }

        managerRepository.delete(manager);
    }
}
