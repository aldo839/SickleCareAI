package com.aldokenfack.SickleCareAI.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(){

        return new OpenAPI()
                .info(new Info()
                        .title("SickleCareAI API")
                        .version("1.0")
                        .description("This is a documentation of the first version of the API for SickleCareAI project.")
                )
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("bearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter your token here.")
                                )
                );
    }

    @Bean
    public OpenApiCustomizer publicRoutesCustomizer(){

        return openAPI -> {
            openAPI.getPaths().forEach((path, item) -> {
                // Remove security requirement for those routes
                if (path.startsWith("/api/auth") ||
                path.equals("/api/patients/register-patient") ||
                path.equals("/api/doctors/register-doctor") ||
                path.equals("/api/admin/register-admin")) {

                    item.readOperations().forEach(op -> op.setSecurity(Collections.emptyList()));
                }
            });
        };
    }

}
