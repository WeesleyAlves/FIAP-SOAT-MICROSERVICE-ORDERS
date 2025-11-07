package br.com.fiap.techchallenge.orders.api.handlers;

import br.com.fiap.techchallenge.orders.api.handlers.common.ApiResponseDTO;
import br.com.fiap.techchallenge.orders.application.controllers.AdminController;
import br.com.fiap.techchallenge.orders.application.dtos.in.PathOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.CompleteOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.QueueOrderDTO;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.OrderDatasource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminHandler {
    private final AdminController adminController;

    public AdminHandler(OrderDatasource orderDatasource) {
        this.adminController = new AdminController(orderDatasource);
    }

    @PostMapping("/reset-queue-number")
    public ResponseEntity<ApiResponseDTO<String>> resetQueueNumber() {
        adminController.resetOrderNumberSequence();

        return ResponseEntity
            .status(200)
            .body(
                ApiResponseDTO.send(200, "Contador reiniciado com sucesso. O próximo pedido começará em 1.")
            );
    }

    @PatchMapping("/order/status")
    public ResponseEntity<ApiResponseDTO<CompleteOrderDTO>> updateOrderStatus(@RequestBody PathOrderDTO dto) {
        var result = adminController.updateStatus( dto );

        return ResponseEntity
            .status(200)
            .body(
                ApiResponseDTO.send(200, "Pedido atualizado com sucesso.", result)
            );
    }

    @GetMapping("/orders")
    public ResponseEntity<ApiResponseDTO<List<QueueOrderDTO>>> getAdminOrdersList() {
        var result = adminController.getAllOrders();

        return ResponseEntity
            .status(200)
            .body(
                ApiResponseDTO.send(200, result.size()+" itens encontrados", result)
            );
    }
}
