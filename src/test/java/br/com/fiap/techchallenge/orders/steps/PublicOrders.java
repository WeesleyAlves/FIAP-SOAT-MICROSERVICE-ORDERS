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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    private String bodyJson;

    private final ObjectMapper mapper = new ObjectMapper();

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

    @Dado("que eu possuo os seguintes dados do pedido:")
    public void queEuPossuoOsSeguintesDadosDoPedido(String json) {
        this.bodyJson = json;

        // Configura o mock de retorno da criação
//        Order pedidoCriado = new Order();
//        pedidoCriado.setId(UUID.randomUUID());
//        pedidoCriado.setOrderNumber(123);
//        pedidoCriado.setStatus("Recebido");
//        pedidoCriado.setCustomerId(UUID.fromString("8c6e378f-9b3c-4e97-9fbe-ffbfb659d15f"));
//        pedidoCriado.setNotes("Com cobertura de morango");
//        pedidoCriado.setCreatedAt(OffsetDateTime.now());
//        pedidoCriado.setUpdatedAt(OffsetDateTime.now());
//        pedidoCriado.setPaymentId(UUID.randomUUID());
//        pedidoCriado.setQrData("qrcode-pix-1234567890");
//
//        List<Product> produtos = Arrays.asList(
//                new Product(UUID.fromString("f809b1c5-6f70-8192-d345-6789012345f0"), "Sorvete de Chocolate", 1, 15.0, 15.0),
//                new Product(UUID.fromString("e7f9a0b4-5e6f-7081-c234-5678901234ef"), "Cobertura de Morango", 1, 5.0, 5.0)
//        );
//
//        pedidoCriado.setProducts(produtos);
//        pedidoCriado.setPrice(produtos.stream().mapToDouble(Product::getTotalValue).sum());
//
//        when(orderService.createOrder(any(Order.class))).thenReturn(pedidoCriado);
    }

    @Quando("eu realizar um requisicao GET para {string}")
    public void realizarRequisicaoGetPara(String endpoint) {
        resposta = restTemplate.getForEntity(endpoint, String.class);
    }

    @Quando("eu realizar uma requisicao POST para {string}")
    public void euRealizarUmaRequisicaoPOSTPara(String path) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(bodyJson, headers);
        resposta = restTemplate.postForEntity(path, request, String.class);
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
