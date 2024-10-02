package com.application.elerna.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${openapi.info.title}")
    private String title;

    @Value("${openapi.info.version}")
    private String version;

    @Value("${openapi.info.description}")
    private String infoDescription;

    @Value("${openapi.server.url}")
    private String url;

    @Value("${openapi.server.description}")
    private String serverDescription;

    @Value("${openapi.service.api-docs}")
    private String apiDocs;

    @Value("${openapi.service.package}")
    private String packageToScan;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info().title(title)
                .version(version)
                .description(infoDescription))
                .servers(List.of(new Server().url(url).description(serverDescription)))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "bearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")))
                .security(List.of(new SecurityRequirement().addList("bearerAuth")));
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group(apiDocs)
                .packagesToScan(packageToScan)
                .build();
    }

}
