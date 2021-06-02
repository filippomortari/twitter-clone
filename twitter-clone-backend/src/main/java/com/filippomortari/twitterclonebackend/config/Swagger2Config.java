package com.filippomortari.twitterclonebackend.config;

import io.swagger.models.auth.In;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;


@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket apis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .securitySchemes(Arrays.asList(new ApiKey("JWT Bearer token", HttpHeaders.AUTHORIZATION, In.HEADER.name())))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.filippomortari.twitterclonebackend.web.rest"))
                .paths(PathSelectors.any())
                .build()
                ;
    }

}

