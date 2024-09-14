package com.ohdaesan.shallwepets.auth.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// http://localhost:8080/swagger-ui/index.html
@OpenAPIDefinition(
        info = @Info(title = "Shall We Pets",
                description = "반려동물과 함께 있을 수 있는 공간을 추천해주는 서비스 API",
                version = "v1"))
@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/api/v1/**", "/member/**"};    // Swagger에서 처리 되었으면 하는 경로 설정
        return GroupedOpenApi.builder()
                .group("api-v1")
                .pathsToMatch(paths)
                .build();
    }
}
