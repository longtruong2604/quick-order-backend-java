package com.backend.backend_java.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

        @Bean
        public OpenAPI openAPI(@Value("${openapi.service.title}") String title,
                        @Value("${openapi.service.version}") String version,
                        @Value("${openapi.service.description}") String description,
                        @Value("${openapi.service.serverUrl}") String server,
                        @Value("${openapi.service.serverName}") String serverName) {
                return new OpenAPI()
                                .info(new Info()
                                                .title(title)
                                                .version(version)
                                                .description(description))
                                .servers(List.of(new Server().url(server).description(serverName)))
                                .components(
                                                new Components().addSecuritySchemes("bearerAuth",
                                                                new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer").bearerFormat("JWT")))
                                .security(List.of(new SecurityRequirement().addList("bearerAuth")));
        }

        @Bean
        public GroupedOpenApi groupedOpenAPI(@Value("${openapi.service.groupName}") String groupName) {
                return GroupedOpenApi.builder()
                                .group(groupName)
                                .packagesToScan("com.backend.backend_java.controller")
                                .build();
        }
}
