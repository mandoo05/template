package dev.hyh.template.domain.member.controller;

import dev.hyh.template.common.response.ApiResponse;
import dev.hyh.template.config.ApiTag;
import dev.hyh.template.domain.member.dto.UserAuthRequest;
import dev.hyh.template.domain.member.service.UserAuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
}
