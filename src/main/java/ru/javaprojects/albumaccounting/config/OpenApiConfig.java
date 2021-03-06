package ru.javaprojects.albumaccounting.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer"
)
@OpenAPIDefinition(
        info = @Info(
                title = "Album Accounting System App Web Service Documentation",
                version = "1.0",
                description = "This page documents Album Accounting System RESTful Web Service endpoints<br><br>" +
                        "To get Authorization JWT token use Profile Controller login endpoint (credentials: admin@gmail.com/admin)",
                license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0"),
                contact = @Contact(url = "https://javaprojects.ru", name = "Igor Shlyakhtenkov", email = "ishlyakhtenkov@yandex.ru")
        ),
        servers = {@Server(url = "https://javaprojects.ru/albumaccounting", description = "Internet Server url"),
                   @Server(url = "https://localhost:8443/albumaccounting", description = "Local Server url")},
        tags = {@Tag(name = "Profile Controller"),
                @Tag(name = "Album Controller"),
                @Tag(name = "Department Controller"),
                @Tag(name = "Employee Controller"),
                @Tag(name = "User Controller (Admin only)")},
        security = @SecurityRequirement(name = "bearerAuth")
)
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("REST API")
                .pathsToMatch("/api/**")
                .build();
    }
}