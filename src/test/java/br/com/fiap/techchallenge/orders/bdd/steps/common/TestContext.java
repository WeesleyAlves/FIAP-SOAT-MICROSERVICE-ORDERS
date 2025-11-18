package br.com.fiap.techchallenge.orders.bdd.steps.common;

import br.com.fiap.techchallenge.orders.application.dtos.out.CompleteOrderDTO;
import io.cucumber.spring.ScenarioScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@ScenarioScope
@Component
public class TestContext {
    private String requestBody;
    private ResponseEntity<String> response;
    private String bodyJson;
    private List<CompleteOrderDTO> existingOrders;


    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public ResponseEntity<String> getResponse() {
        return response;
    }

    public void setResponse(ResponseEntity<String> response) {
        this.response = response;
    }

    public String getBodyJson() {
        return bodyJson;
    }

    public void setBodyJson(String bodyJson) {
        this.bodyJson = bodyJson;
    }

    public List<CompleteOrderDTO> getExistingOrders() {
        return existingOrders;
    }

    public void setExistingOrders(List<CompleteOrderDTO> existingOrders) {
        this.existingOrders = existingOrders;
    }
}
