package dev.hyh.template.domain.member.infra;

import dev.hyh.template.domain.member.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE member SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, updatable = false)
    private String username;

    private String password;

    @Column(nullable = false)
    private String name;

    private Role role;
}
