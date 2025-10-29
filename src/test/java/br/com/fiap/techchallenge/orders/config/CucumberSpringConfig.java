package br.com.fiap.techchallenge.orders.config;

import org.springframework.boot.test.context.SpringBootTest;
import io.cucumber.spring.CucumberContextConfiguration;
import br.com.fiap.techchallenge.orders.FastfoodOrdersApplication;

@CucumberContextConfiguration
@SpringBootTest(classes = FastfoodOrdersApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberSpringConfig {
}
