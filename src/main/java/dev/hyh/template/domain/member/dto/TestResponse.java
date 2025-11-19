package dev.hyh.template.domain.member.dto;

import dev.hyh.template.domain.member.infra.User;
import lombok.Builder;

import java.util.UUID;

public class TestResponse {

    @Builder
    public record testGetResponse(
            UUID id,
            String userId,
            String name
    ) {
        public static testGetResponse from(User user) {
            return testGetResponse.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .userId(user.getUsername())
                    .build();
        }
    }
}
