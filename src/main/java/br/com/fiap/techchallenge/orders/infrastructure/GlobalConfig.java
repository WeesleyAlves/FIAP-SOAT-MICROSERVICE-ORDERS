package br.com.fiap.techchallenge.orders.infrastructure;

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
}
