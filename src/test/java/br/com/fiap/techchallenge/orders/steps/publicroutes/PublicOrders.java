package br.com.fiap.techchallenge.orders.steps.publicroutes;

import br.com.fiap.techchallenge.orders.application.dtos.out.CompleteOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.OrderProductItemOutDTO;
import br.com.fiap.techchallenge.orders.infrastructure.adapters.OrderAdapter;
import br.com.fiap.techchallenge.orders.steps.common.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;


public class PublicOrders {
    @Autowired
    private TestContext context;


    @Autowired
    private OrderAdapter orderAdapter;

    private CompleteOrderDTO pedidoDTO;


    @Dado("que existe um pedido com os seguintes dados:")
    public void queExisteUmPedidoComOsSeguintesDados(DataTable dataTable) {
        Map<String, String> dados = dataTable.asMaps().getFirst();

        pedidoDTO = new CompleteOrderDTO(
                UUID.fromString( dados.get("id") ),
                Integer.valueOf( dados.get("order_number") ),
                dados.get("status"),
                UUID.fromString( dados.get("customer_id") ),
                dados.get("notes"),
                new BigDecimal( dados.get("original_price") ),
                OffsetDateTime.parse(dados.get("created_at")).toLocalDateTime(),
                OffsetDateTime.parse(dados.get("updated_at")).toLocalDateTime(),
                Optional.empty(),
                Optional.of(UUID.fromString(dados.get("payment_id"))),
                Optional.of( dados.get( "payment_qr_data" ) )
        );
    }

    @E("os seguintes produtos:")
    public void osSeguintesProdutos(DataTable dataTable) {
        List<OrderProductItemOutDTO> produtos = dataTable.asMaps().stream()
                .map(map -> new OrderProductItemOutDTO(
                        UUID.fromString(map.get("id")),
                        map.get("name"),
                        new BigDecimal( map.get("price") ),
                        Integer.parseInt( map.get("quantity") ),
                        new BigDecimal( map.get("totalValue") )
                ))
                .toList();

        pedidoDTO = new CompleteOrderDTO(
                pedidoDTO.id(),
                pedidoDTO.order_number(),
                pedidoDTO.status(),
                pedidoDTO.customer_id(),
                pedidoDTO.notes(),
                pedidoDTO.price(),
                pedidoDTO.created_at(),
                pedidoDTO.updated_at(),
                Optional.of( produtos ),
                pedidoDTO.payment_id(),
                pedidoDTO.payment_qr_data()
        );




        System.out.println( "payment id : "+pedidoDTO.payment_id() );
        System.out.println( "payment qr : "+pedidoDTO.payment_qr_data() );

        Mockito.when( orderAdapter.findOrderByID( pedidoDTO.id() ) )
            .thenReturn( pedidoDTO );
    }

    @Dado("que eu possuo os seguintes dados do pedido:")
    public void queEuPossuoOsSeguintesDadosDoPedido(String json) {
        context.setBodyJson( json );

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
}
