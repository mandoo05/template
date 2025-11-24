package dev.hyh.template.domain.member.infra.repository;

import dev.hyh.template.domain.member.infra.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByUsername(String username);
}
