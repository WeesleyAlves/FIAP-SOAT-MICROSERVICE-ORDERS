package br.com.fiap.techchallenge.orders.api.handlers;

import br.com.fiap.techchallenge.orders.api.handlers.common.ApiResponseDTO;
import br.com.fiap.techchallenge.orders.application.controllers.AdminController;
import br.com.fiap.techchallenge.orders.application.dtos.in.PathOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.CompleteOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.QueueOrderDTO;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.OrderDatasource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(
        name = "Administração dos Pedidos",
        description = "Rotas administrativas para gerenciamento de pedidos, fila e status."
)
public class AdminHandler {

    private final AdminController adminController;

    public AdminHandler(OrderDatasource orderDatasource) {
        this.adminController = new AdminController(orderDatasource);
    }

    @Operation(
            summary = "Reinicia o contador da fila",
            description = "Zera o contador de pedidos. O próximo pedido receberá número 1.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Contador reiniciado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno ao tentar reiniciar o contador",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class)
                            )
                    )
            }
    )
    @PostMapping("/reset-queue-number")
    public ResponseEntity<ApiResponseDTO<String>> resetQueueNumber() {
        adminController.resetOrderNumberSequence();

        return ResponseEntity
            .status(200)
            .body(
                ApiResponseDTO.send(200, "Contador reiniciado com sucesso. O próximo pedido começará em 1.")
            );
    }

    @Operation(
            summary = "Atualiza o status de um pedido",
            description = "Modifica o status de um pedido existente, retornando o pedido atualizado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Status atualizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CompleteOrderDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação ou dados incorretos",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno ao atualizar o status",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class)
                            )
                    )
            }
    )
    @PatchMapping("/order/status")
    public ResponseEntity<ApiResponseDTO<CompleteOrderDTO>> updateOrderStatus(@RequestBody PathOrderDTO dto) {
        var result = adminController.updateStatus( dto );

        return ResponseEntity
            .status(200)
            .body(
                ApiResponseDTO.send(200, "Pedido atualizado com sucesso.", result)
            );
    }

    @Operation(
            summary = "Lista os pedidos administrativos",
            description = "Retorna todos os pedidos, com informações completas de posição e status.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista recuperada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = QueueOrderDTO.class))
                            )
                    )
            }
    )
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
