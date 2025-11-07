package br.com.fiap.techchallenge.orders.api.handlers;

import br.com.fiap.techchallenge.orders.api.handlers.common.ApiResponseDTO;
import br.com.fiap.techchallenge.orders.application.controllers.PublicController;
import br.com.fiap.techchallenge.orders.application.dtos.in.NewOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.CompleteOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.QueueOrderDTO;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.InventoryDatasource;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.OrderDatasource;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.PaymentDatasource;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.ProductsDatasource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class PublicHandler {

    private final PublicController publicController;

    public PublicHandler(
            OrderDatasource orderDatasource,
            PaymentDatasource paymentDatasource,
            ProductsDatasource productsDatasource,
            InventoryDatasource inventoryDatasource
    ) {
        this.publicController = new PublicController(
            orderDatasource,
            paymentDatasource,
            productsDatasource,
            inventoryDatasource
        );
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<ApiResponseDTO<CompleteOrderDTO>> getById(@PathVariable UUID id) {
        var response = publicController.getOrderById(id);

        return ResponseEntity
            .status(200)
            .body(
                ApiResponseDTO.send(200, "Pedido encontrado com sucesso", response)
            );
    }

    @GetMapping("/queue")
    public ResponseEntity<ApiResponseDTO<List<QueueOrderDTO>>> listQueue() {
        var response = publicController.getPublicOrders();

        return ResponseEntity
            .status(200)
            .body(
                ApiResponseDTO.send(200, response.size()+" itens encontrados", response)
            );
    }

    @PostMapping("/order")
    public ResponseEntity<ApiResponseDTO<CompleteOrderDTO>> createOrder(@RequestBody NewOrderDTO newOrderDTO) {
        var response = publicController.createOrder(newOrderDTO);

        return ResponseEntity
            .status(201)
            .body(
                ApiResponseDTO.send(201,"Pedido criado com sucesso", response)
            );
    }
}
