package br.com.dbserver.desafio_votacao.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Desafio Votacão API",
                version = "1.0.0",
                description = "API do desafio de votação"
        )
)
public class OpenApiConfig {
}
