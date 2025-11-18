package dev.hyh.template.domain.member.dto;

public class UserAuthRequest {
    public record UserPostRequest(
            String username,
            String password,
            String name) {
    }
}
