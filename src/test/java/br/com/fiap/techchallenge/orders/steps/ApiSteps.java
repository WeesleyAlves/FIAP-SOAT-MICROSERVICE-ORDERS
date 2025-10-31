package br.com.fiap.techchallenge.orders.steps;

import br.com.fiap.techchallenge.orders.FastfoodOrdersApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

@SpringBootTest(classes = FastfoodOrdersApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ApiSteps {

    @Autowired
    protected TestRestTemplate restTemplate;

    protected ResponseEntity<String> response;
}
