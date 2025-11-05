package br.com.fiap.techchallenge.orders.steps.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;

import java.io.InputStream;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestsSteps {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestContext context;

    @Quando("eu realizar um requisicao GET para {string}")
    public void realizarRequisicaoGetPara(String endpoint) {
        context.setResponse(
            restTemplate.getForEntity(endpoint, String.class)
        );
    }

    @Quando("eu realizar uma requisicao POST para {string}")
    public void euRealizarUmaRequisicaoPOSTPara(String path) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(context.getBodyJson(), headers);

        context.setResponse(
            restTemplate.postForEntity(path, request, String.class)
        );
    }

    @Quando("eu realizar uma requisicao PATCH para {string}")
    public void euRealizarUmaRequisicaoPATCHPara(String path) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(context.getBodyJson(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
            path,
            HttpMethod.PATCH,
            request,
            String.class
        );

        context.setResponse(response);
    }

    @Entao("deve retornar status {int}")
    public void deveRetornarStatus(int statusEsperado) {
        assertThat(
            context.getResponse().getStatusCode().value()
        ).isEqualTo(statusEsperado);
    }

    @Entao("deve retornar um JSON com o schema {string}")
    public void deveConterUmJsonComOsCamposEsperados(String nomeSchema) throws Exception {
        String json = context.getResponse().getBody();

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
