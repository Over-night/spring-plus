package org.example.expert.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.entity.Timestamped;
import org.example.expert.domain.user.enums.UserRole;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(unique = false, nullable = false)
    private String nickname;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", length = 20, nullable = false)
    private UserRole userRole;

    public User(String email, String password, String nickname, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.userRole = userRole;
    }

    private User(Long id, String email, String nickname, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.userRole = userRole;
    }

    public static User fromAuthUser(AuthUser authUser) {
        return new User(authUser.getId(), authUser.getEmail(), authUser.getNickname(),
                authUser.getAuthorities().stream()
                        .findFirst()
                        .map(a -> UserRole.valueOf(a.getAuthority()))
                        .orElseThrow(() -> new AuthException("권한이 존재하지 않습니다.")));
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void updateRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
