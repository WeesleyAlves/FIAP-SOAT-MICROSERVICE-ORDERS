package br.com.fiap.techchallenge.orders.steps.adminroutes;

import br.com.fiap.techchallenge.orders.steps.common.TestContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminOrders {
    @Autowired
    private TestContext context;

    @Dado("que existe um pedido com o seguinte o ID {string}")
    public void queExisteUmPedidoComOSeguinteID(String id) {
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

    @E("os seguintes dados para a atualizacao:")
    public void osSeguintesDadosParaAtualizacao(String json) {
        context.setBodyJson( json );
    }
}
