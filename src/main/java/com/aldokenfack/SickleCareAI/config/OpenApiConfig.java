package com.aldokenfack.SickleCareAI.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

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

}
