package ru.isntrui.sharik.configurations

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfiguration {
    @Bean
    fun customOpenAPI(): OpenAPI? {
        return OpenAPI()
            .info(
                Info()
                    .title("Sharik API")
                    .version("0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.1")
                    .description(
                        """
                                API for Sharik app.
                                Made with Spring Boot and Kotlin.
                                PROD contest Hackaton, Moscow, Nov 2024
                                
                                To test it select some endpoint bellow and click "Try it out" or just use Postman.
                                
                                OAS-file is http://[host]:[port]/api/oas31
                                
                                Made by team 12 (мы не умеем считать)
                                
                                """.trimIndent()
                    )
                    .contact(
                        Contact()
                            .name("GitLab")
                            .url("https://prod-hack.gitlab.yandexcloud.net/prod-team-12/shark_serv")
                    )
                    .license(
                        License()
                            .name("WTFPL")
                            .url("http://www.wtfpl.net")
                    )
            )
    }
}