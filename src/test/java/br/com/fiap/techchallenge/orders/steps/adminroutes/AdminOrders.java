package br.com.fiap.techchallenge.orders.steps.adminroutes;

import br.com.fiap.techchallenge.orders.application.dtos.out.CompleteOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.OrderNumberDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.OrderProductItemOutDTO;
import br.com.fiap.techchallenge.orders.infrastructure.adapters.OrderAdapter;
import br.com.fiap.techchallenge.orders.steps.common.TestContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

public class AdminOrders {
    @Autowired
    private TestContext context;

    @Autowired
    private OrderAdapter orderAdapter;

    CompleteOrderDTO existingOrderDTO;

    @Dado("que preciso reiniciar a fila de pedidos")
    public void quePrecisaReiniciarAFilaDePedidos() {
        Mockito.when(orderAdapter.findTopOrderNumber()).thenReturn(new OrderNumberDTO(
                3L,
                3
        ));

        Mockito.when(orderAdapter.saveOrderNumber( any() ) )
            .thenReturn( new OrderNumberDTO(
                1L,
                0
            ));
    }

    @Dado("que existe um pedido com o seguinte o ID {string}")
    public void queExisteUmPedidoComOSeguinteID(String id) {
        List<OrderProductItemOutDTO> products = new ArrayList<>();

        products.add(new OrderProductItemOutDTO(
                UUID.randomUUID(),
                "Suco de laranja",
                new BigDecimal("10.00"),
                2,
                new BigDecimal("20.00")
        ));

        existingOrderDTO = new CompleteOrderDTO(
                UUID.fromString( id ),
                3,
                "Aguardando Pagamento",
                UUID.randomUUID(),
                "",
                new BigDecimal("20.00"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                Optional.of(products),
                Optional.empty(),
                Optional.empty()
        );

        Mockito.when(orderAdapter.findOrderByID( UUID.fromString(id) ) )
            .thenReturn(existingOrderDTO);
    }

    @E("os seguintes dados para a atualizacao:")
    public void osSeguintesDadosParaAtualizacao(String json) {
        context.setBodyJson( json );

        existingOrderDTO = existingOrderDTO.withStatus("Recebido");


        Mockito.when(orderAdapter.updateOrder( any() ) )
            .thenReturn(existingOrderDTO);
    }
}
