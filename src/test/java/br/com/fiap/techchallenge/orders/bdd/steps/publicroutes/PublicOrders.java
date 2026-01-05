package br.com.fiap.techchallenge.orders.bdd.steps.publicroutes;

import br.com.fiap.techchallenge.orders.application.dtos.in.PaymentInDTO;
import br.com.fiap.techchallenge.orders.application.dtos.in.ProductInDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.CompleteOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.CreateOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.OrderNumberDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.OrderProductItemOutDTO;
import br.com.fiap.techchallenge.orders.infrastructure.adapters.OrderAdapter;
import br.com.fiap.techchallenge.orders.infrastructure.adapters.PaymentAdapter;
import br.com.fiap.techchallenge.orders.infrastructure.adapters.ProductsAdapter;
import br.com.fiap.techchallenge.orders.bdd.steps.common.TestContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;


public class PublicOrders {
    @Autowired
    private TestContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderAdapter orderAdapter;

    @Autowired
    private ProductsAdapter productsAdapter;

    @Autowired
    private PaymentAdapter paymentAdapter;

    private CompleteOrderDTO orderByIdDTO;

    List<CompleteOrderDTO> ordersQueue;

    @Dado("que existe um pedido com os seguintes dados:")
    public void queExisteUmPedidoComOsSeguintesDados(DataTable dataTable) {
        Map<String, String> dados = dataTable.asMaps().getFirst();

        orderByIdDTO = new CompleteOrderDTO(
                UUID.fromString(dados.get("id")),
                Integer.valueOf(dados.get("order_number")),
                dados.get("status"),
                UUID.fromString(dados.get("customer_id")),
                dados.get("notes"),
                new BigDecimal(dados.get("original_price")),
                OffsetDateTime.parse(dados.get("created_at")).toLocalDateTime(),
                OffsetDateTime.parse(dados.get("updated_at")).toLocalDateTime(),
                Optional.empty(),
                Optional.of(UUID.fromString(dados.get("payment_id"))),
                Optional.of(dados.get("payment_qr_data"))
        );
    }

    @E("os seguintes produtos:")
    public void osSeguintesProdutos(DataTable dataTable) {
        List<OrderProductItemOutDTO> produtos = dataTable.asMaps().stream()
                .map(map -> new OrderProductItemOutDTO(
                        UUID.fromString(map.get("id")),
                        map.get("name"),
                        new BigDecimal(map.get("price")),
                        Integer.parseInt(map.get("quantity")),
                        new BigDecimal(map.get("totalValue"))
                ))
                .toList();

        orderByIdDTO = new CompleteOrderDTO(
                orderByIdDTO.id(),
                orderByIdDTO.order_number(),
                orderByIdDTO.status(),
                orderByIdDTO.customer_id(),
                orderByIdDTO.notes(),
                orderByIdDTO.price(),
                orderByIdDTO.created_at(),
                orderByIdDTO.updated_at(),
                Optional.of(produtos),
                orderByIdDTO.payment_id(),
                orderByIdDTO.payment_qr_data()
        );

        Mockito.when(orderAdapter.findOrderByID(orderByIdDTO.id()))
                .thenReturn(orderByIdDTO);

        Mockito.when(paymentAdapter.getPaymentByOrderId(orderByIdDTO.id()))
                .thenReturn(new PaymentInDTO(
                        orderByIdDTO.payment_id().orElseThrow(),
                        orderByIdDTO.payment_qr_data().orElseThrow()
                ));
    }

    @Dado("que eu possuo os seguintes dados do pedido:")
    public void queEuPossuoOsSeguintesDadosDoPedido(String json) throws JsonProcessingException {
        context.setBodyJson(json);

        var inputOrder = objectMapper.readValue(json, CreateOrderDTO.class);
        LocalDateTime fixedTime = LocalDateTime.now();
        String qrData = "qr_data_12347890345";
        UUID paymentId = UUID.randomUUID();

        var productsList = new ArrayList<OrderProductItemOutDTO>();

        productsList.add(new OrderProductItemOutDTO(
            UUID.fromString("84edb96f-c2e5-425b-a26e-ae583ff19d31"),
            "X-Burger",
            BigDecimal.valueOf(15.90),
            1,
            BigDecimal.valueOf(15.90)
        ));

        productsList.add(new OrderProductItemOutDTO(
            UUID.fromString("ba931dea-9fba-4587-82c7-b724069051f8"),
            "Refrigerante",
            BigDecimal.valueOf(8.50),
            1,
            BigDecimal.valueOf(8.50)
        ));

        var expectedSavedOrder = new CompleteOrderDTO(
                UUID.randomUUID(),
                1,
                "Recebido",
                UUID.randomUUID(),
                inputOrder.notes(),
                BigDecimal.valueOf(10.00),
                fixedTime,
                fixedTime,
                Optional.of(productsList),
                Optional.of(paymentId),
                Optional.of(qrData)
        );

        Mockito.when(orderAdapter.findTopOrderNumber()).thenReturn(new OrderNumberDTO(
            0L,
            0
        ));

        Mockito.when(orderAdapter.saveOrderNumber( any() ) ).thenReturn(
                new OrderNumberDTO(
                    2L,
                    2
                )
        );

        Mockito.when( paymentAdapter.createPayment( any() ) )
                .thenReturn( new PaymentInDTO(
                    paymentId,
                    qrData
                ));


        Mockito.when(orderAdapter.saveOrder(any(CreateOrderDTO.class)))
                .thenReturn(expectedSavedOrder);
    }

    @E("os seguintes produtos cadastrados:")
    public void osSeguintesProdutosCadastrados(DataTable dataTable) {
        List<ProductInDTO> productsList = dataTable.asMaps().stream()
                .map(map -> new ProductInDTO(
                        UUID.fromString(map.get("id")),
                        map.get("name"),
                        new BigDecimal(map.get("price"))
                ))
                .toList();

        var productsIds = productsList.stream().map(ProductInDTO::id).toList();

        Mockito.when(productsAdapter.getAllByIds(productsIds))
                .thenReturn(productsList);
    }
}
