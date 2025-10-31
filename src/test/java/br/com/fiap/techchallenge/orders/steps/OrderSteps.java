package br.com.fiap.techchallenge.orders.steps;

import br.com.fiap.techchallenge.orders.FastfoodOrdersApplication;
import io.cucumber.java.it.Quando;
import io.cucumber.java.pt.Entao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = FastfoodOrdersApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<String> response;

    @Quando("eu faço uma requisição GET para {string}")
    public void eu_faco_uma_requisicao_get_para(String endpoint) {
        response = restTemplate.getForEntity(endpoint, String.class);
    }

    @Entao("o retorno deve conter {string}")
    public void o_retorno_deve_conter(String message) {
        assertThat(response.getBody()).contains(message);
    }
}
