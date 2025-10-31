package br.com.fiap.techchallenge.orders.steps;

import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.junit.jupiter.api.Assertions;

public class OrderSteps extends ApiSteps {

    @Quando("eu faço uma requisição GET para {string}")
    public void euFacoUmaRequisicaoGETPara(String endpoint) {
        response = restTemplate.getForEntity(endpoint, String.class);
    }

    @Entao("o status deve ser {int}")
    public void oStatusDeveSer(int statusCode) {
        Assertions.assertEquals(statusCode, response.getStatusCode().value());
    }

    @Entao("o corpo deve conter {string}")
    public void oCorpoDeveConter(String esperado) {
        Assertions.assertTrue(response.getBody().contains(esperado));
    }
}
