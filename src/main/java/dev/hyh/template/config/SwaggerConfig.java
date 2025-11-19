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
    public GroupedOpenApi defaultApi() {
        return GroupedOpenApi.builder()
                .group("default")
                .packagesToScan("dev.hyh.template")     // 전체 스캔
                .pathsToExclude("/docs/error/**")       // 에러 문서 제외
                .addOperationCustomizer((operation, handlerMethod) -> {

                    Optional.ofNullable(handlerMethod.getBeanType().getAnnotation(ApiTag.class))
                            .ifPresent(apiTag -> operation.addTagsItem(apiTag.name()));

                    PreAuthorize methodAuth = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), PreAuthorize.class);
                    PreAuthorize classAuth = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), PreAuthorize.class);

                    // 인증이 필요한 경우만 Swagger에 Security 표시
                    if (isSecured(methodAuth) || isSecured(classAuth)) {
                        operation.addSecurityItem(
                                new io.swagger.v3.oas.models.security.SecurityRequirement().addList("bearerAuth")
                        );
                    }

                    return operation;
                })
                .build();
    }

    @Bean
    public GroupedOpenApi errorApi() {
        return GroupedOpenApi.builder()
                .group("Error")                    // Swagger UI 좌측에 Error 그룹 생성
                .pathsToMatch("/docs/error/**")     // 에러코드 API만 포함
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
