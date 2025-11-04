package br.com.fiap.techchallenge.orders.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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

//    @MockitoBean
//    private OrderService orderService;
//    private Order pedidoMock;

    @Dado("que existe um pedido com os seguintes dados:")
    public void queExisteUmPedidoComOsSeguintesDados(DataTable dataTable) {
//        Map<String, String> dados = dataTable.asMaps().get(0);
//
//        pedidoMock = new Order();
//        pedidoMock.setId(UUID.fromString(dados.get("id")));
//        pedidoMock.setOrderNumber(Integer.parseInt(dados.get("order_number")));
//        pedidoMock.setStatus(dados.get("status"));
//        pedidoMock.setCustomerId(UUID.fromString(dados.get("customer_id")));
//        pedidoMock.setNotes(dados.get("notes"));
//        pedidoMock.setCreatedAt(OffsetDateTime.parse(dados.get("created_at")));
//        pedidoMock.setUpdatedAt(OffsetDateTime.parse(dados.get("updated_at")));
//        pedidoMock.setPaymentId(UUID.fromString(dados.get("payment_id")));
//        pedidoMock.setQrData(dados.get("qr_data"));
//
//        pedidoMock.setProducts(new ArrayList<>()); // vai ser preenchido depois
    }

    @E("os seguintes produtos:")
    public void osSeguintesProdutos(DataTable dataTable) {
//        List<Product> produtos = dataTable.asMaps().stream()
//                .map(map -> new Product(
//                        UUID.fromString(map.get("id")),
//                        map.get("name"),
//                        Integer.parseInt(map.get("quantity")),
//                        Double.parseDouble(map.get("price")),
//                        Double.parseDouble(map.get("totalValue"))
//                ))
//                .collect(Collectors.toList());
//
//        pedidoMock.setProducts(produtos);
//
//        // Calcula o preço total automaticamente
//        double total = produtos.stream()
//                .mapToDouble(Product::getTotalValue)
//                .sum();
//        pedidoMock.setPrice(total);
//
//        // Mocka o service
//        Mockito.when(orderService.findById(eq(pedidoMock.getId())))
//                .thenReturn(pedidoMock);
    }

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
