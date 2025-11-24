package dev.hyh.template.domain.member.infra.entity;

import dev.hyh.template.common.BaseSoftDeleteTimeEntity;
import dev.hyh.template.domain.member.dto.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
@SQLDelete(sql = "UPDATE member SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Member extends BaseSoftDeleteTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "uuid DEFAULT uuid_v7()")
    private UUID id;

    @Column(nullable = false, unique = true, updatable = false)
    private String username;

    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    public void updatePassword(String password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder != null ? passwordEncoder.encode(password) : password;
    }
}
