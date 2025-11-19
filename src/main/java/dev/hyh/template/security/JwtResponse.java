package dev.hyh.template.security;

import lombok.Builder;

@Builder
public record JwtResponse(String accessToken) {

    public static JwtResponse from(String accessToken) {
        return JwtResponse.builder()
                .accessToken("Bearer " + accessToken)
                .build();
    }
}
