package com.open.poker.swagger;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.providers.SpringWebProvider;
import org.springdoc.webmvc.ui.SwaggerWelcomeWebMvc;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringFoxConfig {

    @Bean
    public GroupedOpenApi swaggerApi() {
        return GroupedOpenApi.builder().group("public").pathsToMatch("/**").packagesToScan("com.open.poker").build();
    }

    @Bean
    public OpenAPI apiEndPointsInfo() {
        return new OpenAPI().info(new Info().title("Auth Service REST API")
                .description("Authentication REST End Points")
                .contact(new Contact().name("Rishabh Daim").url("www.javaguides.net").email("rishabhdaim1991@gmail.com"))
                .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                .version("1.0.0"))
                .externalDocs(new ExternalDocumentation()
                .description("SpringShop Wiki Documentation")
                .url("https://springshop.wiki.github.org/docs"));
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            name = {"springdoc.use-management-port"},
            havingValue = "false",
            matchIfMissing = true
    )
    SwaggerWelcomeWebMvc swaggerWelcome(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties, SwaggerUiConfigParameters swaggerUiConfigParameters, SpringWebProvider springWebProvider) {
        return new SwaggerWelcomeWebMvc(swaggerUiConfig, springDocConfigProperties, swaggerUiConfigParameters, springWebProvider);
    }
}
