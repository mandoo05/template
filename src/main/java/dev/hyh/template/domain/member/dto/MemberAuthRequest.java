package dev.hyh.template.domain.member.dto;

public class MemberAuthRequest {
    public record MemberPostRequest(
            String username,
            String password,
            String name) {
    }
}
