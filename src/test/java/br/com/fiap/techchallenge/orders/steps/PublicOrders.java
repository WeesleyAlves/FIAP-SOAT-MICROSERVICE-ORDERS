package br.com.fiap.techchallenge.orders.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import io.cucumber.java.pt.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PublicOrders {

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<String> resposta;

    @Quando("eu realizar um requisicao GET para {string}")
    public void realizarRequisicaoGetPara(String endpoint) {
        resposta = restTemplate.getForEntity(endpoint, String.class);
    }

    @Entao("deve retornar status {int}")
    public void deveRetornarStatus(int statusEsperado) {
        assertThat(resposta.getStatusCode().value()).isEqualTo(statusEsperado);
    }

    @Entao("deve retornar um JSON com o schema {string}")
    public void deveConterUmJsonComOsCamposEsperados(String nomeSchema) throws Exception {
        String json = resposta.getBody();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);

        // Carrega o schema
        InputStream schemaStream = new ClassPathResource("schemas/"+nomeSchema).getInputStream();
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);
        JsonSchema schema = schemaFactory.getSchema(schemaStream);


        Set<ValidationMessage> erros = schema.validate(jsonNode);

        assertThat(erros)
                .as("Erros de validação do JSON Schema")
                .isEmpty();
    }
}
