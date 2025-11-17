package dev.hyh.template.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {

    /**
     * 🔹 OpenAPI 기본 설정 (전역 보안 제거)
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new io.swagger.v3.oas.models.servers.Server().url("/"));
    }

    /**
     * 🔹 @PreAuthorize 기반으로 API별 자물쇠 표시
     */
    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("default")
                .addOperationCustomizer((operation, handlerMethod) -> {

                    // ApiTag 추가
                    Optional.ofNullable(handlerMethod.getBeanType().getAnnotation(ApiTag.class))
                            .ifPresent(apiTag -> operation.addTagsItem(apiTag.name()));

                    // 메서드/클래스 단위의 @PreAuthorize 탐지
                    PreAuthorize methodAuth = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), PreAuthorize.class);
                    PreAuthorize classAuth = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), PreAuthorize.class);

                    // 인증이 필요한 경우에만 bearerAuth 보안 추가
                    if (isSecured(methodAuth) || isSecured(classAuth)) {
                        operation.addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("bearerAuth"));
                    }

                    return operation;
                })
                .build();
    }

    /**
     * 🔸 인증/인가가 필요한 @PreAuthorize 조건 판단
     */
    private boolean isSecured(PreAuthorize preAuthorize) {
        if (preAuthorize == null) return false;

        String value = preAuthorize.value().trim();

        // permitAll, anonymous → 보안 표시 제외
        if (value.contains("permitAll") || value.contains("anonymous")) return false;

        // isAuthenticated, hasRole, hasAnyRole → 보안 표시
        return value.contains("isAuthenticated()")
                || value.contains("hasRole")
                || value.contains("hasAnyRole");
    }
}
