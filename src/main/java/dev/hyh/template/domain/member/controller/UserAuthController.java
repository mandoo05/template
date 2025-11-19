package dev.hyh.template.domain.member.controller;

import dev.hyh.template.common.response.ApiResponse;
import dev.hyh.template.config.ApiTag;
import dev.hyh.template.domain.member.dto.TestResponse;
import dev.hyh.template.domain.member.dto.UserAuthRequest;
import dev.hyh.template.domain.member.service.UserAuthService;
import dev.hyh.template.security.JwtResponse;
import dev.hyh.template.security.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
@ApiTag(name = "유저 API")
public class UserAuthController {
    private final UserAuthService userAuthService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ApiResponse<Void> save(@RequestBody UserAuthRequest.UserPostRequest dto) {
        userAuthService.save(dto);
        return ApiResponse.noContentSuccess();
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<TestResponse.testGetResponse> get(@AuthenticationPrincipal CustomUserDetails userInfo) {
        var data = userAuthService.get(UUID.fromString(userInfo.getUserId()));
        return ApiResponse.success(data);
    }

    @Operation(summary = "Jwt 재발급")
    @PostMapping("/refresh")
    public ApiResponse<JwtResponse> refreshToken(
            @RequestHeader("Authorization") String refreshTokenHeader
    ) {
        String newAccessToken = userAuthService.reissueAccessToken(refreshTokenHeader);
        return ApiResponse.success(JwtResponse.from(newAccessToken));
    }
}
