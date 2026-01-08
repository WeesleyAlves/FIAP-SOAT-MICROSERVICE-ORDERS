package br.com.fiap.techchallenge.orders.infrastructure;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GlobalConfig {

    // Define o Bean do RestTemplate para ser injetado nas classes que o solicitarem
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("FIAP Fastfood - Microsserviço de Pedidos")
                        .description("Documentação da API")
                        .version("1.0.0")
                );
    }
}
