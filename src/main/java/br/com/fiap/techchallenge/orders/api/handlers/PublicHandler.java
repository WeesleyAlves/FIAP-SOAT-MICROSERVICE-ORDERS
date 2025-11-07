package br.com.fiap.techchallenge.orders.api.handlers;

import br.com.fiap.techchallenge.orders.api.handlers.common.ApiResponseDTO;
import br.com.fiap.techchallenge.orders.application.controllers.PublicController;
import br.com.fiap.techchallenge.orders.application.dtos.in.NewOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.CompleteOrderDTO;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.InventoryDatasource;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.OrderDatasource;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.PaymentDatasource;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.ProductsDatasource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("");
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<String> getById(@PathVariable String id) {
        return ResponseEntity.ok("");
    }

    @GetMapping("/queue")
    public ResponseEntity<ApiResponseDTO<String>> listQueue() {
//        var response = publicController.getPublicOrders();

        return ResponseEntity
            .status(200)
            .body(
                    ApiResponseDTO.send(200, 10+" itens encontrados", "")
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
