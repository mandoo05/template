package dev.hyh.template.common.swagger;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorCodeResponse {
    private String name; // AUTH001
    private String code; // "AUTH001"
    private String description; // "아이디 또는 비밀번호가 잘못됨"
}