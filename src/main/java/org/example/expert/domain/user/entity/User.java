package org.example.expert.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.entity.Timestamped;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.enums.UserRole;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 생성자 레벨을 protected 설정해 객체 생성을 막음
@Table(name = "users")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public User(String email, String password, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    private User(Long id, String email, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.userRole = userRole;
    }

    /**
     * AuthService 에서 User 생성시 사용
     *
     * @param email
     * @param encodedPassword
     * @param userRole
     * @return
     */
    public static User of(String email, String encodedPassword, UserRole userRole) {
        return new User(email, encodedPassword, userRole);
    }

    public static User fromAuthUser(AuthUser authUser) {
        return new User(authUser.getId(), authUser.getEmail(), authUser.getUserRole());
    }

    public void changePassword(String newPassword, String oldPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(oldPassword, this.password)) {
            throw new InvalidRequestException("잘못된 비밀번호입니다.");
        }

        if (passwordEncoder.matches(newPassword, this.password)) {
            throw new InvalidRequestException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
        }
        this.password = passwordEncoder.encode(newPassword);
    }

    public void updateRole(UserRole userRole) {
        this.userRole = userRole;
    }

}
