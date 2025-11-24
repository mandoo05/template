package dev.hyh.template.domain.member.controller;

import dev.hyh.template.common.response.ApiResponse;
import dev.hyh.template.config.ApiTag;
import dev.hyh.template.domain.member.dto.MemberAuthRequest;
import dev.hyh.template.domain.member.service.UserAuthService;
import dev.hyh.template.security.JwtResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
@ApiTag(name = "유저 API")
public class MemberAuthController {
    private final UserAuthService userAuthService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ApiResponse<Void> save(@RequestBody MemberAuthRequest.MemberPostRequest dto) {
        userAuthService.save(dto);
        return ApiResponse.noContentSuccess();
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
