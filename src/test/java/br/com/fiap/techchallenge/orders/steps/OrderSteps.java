package br.com.fiap.techchallenge.orders.steps;

import br.com.fiap.techchallenge.orders.FastfoodOrdersApplication;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
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

    @When("I do a GET request to {string}")
    public void i_do_a_get_request_to(String endpoint) {
        response = restTemplate.getForEntity(endpoint, String.class);
    }

    @Then("the return is {string}")
    public void the_return_is(String message) {
        assertThat(response.getBody()).contains(message);
    }
}
