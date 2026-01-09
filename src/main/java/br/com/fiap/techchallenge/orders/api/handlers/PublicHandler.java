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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(
        name = "Zona pública de Pedidos",
        description = "Rotas públicas para os pedidos"
)
@RequestMapping("/api/v1/orders")
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

    @Operation(
            summary = "Busca um pedido pelo ID",
            description = "Retorna um pedido completo",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Pedido encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CompleteOrderDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Pedido não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class)
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CompleteOrderDTO>> getById(@PathVariable UUID id) {
        var response = publicController.getOrderById(id);

        return ResponseEntity
            .status(200)
            .body(
                ApiResponseDTO.send(200, "Pedido encontrado com sucesso", response)
            );
    }

    @Operation(
            summary = "Lista pedidos em fila pública",
            description = "Retorna uma lista de pedidos com posição e status",
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
    @GetMapping("/queue")
    public ResponseEntity<ApiResponseDTO<List<QueueOrderDTO>>> listQueue() {
        var response = publicController.getPublicOrders();

        return ResponseEntity
            .status(200)
            .body(
                ApiResponseDTO.send(200, response.size()+" itens encontrados", response)
            );
    }

    @Operation(
            summary = "Cria um novo pedido",
            description = "Retorna o pedido criado",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Pedido criado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CompleteOrderDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Erro ao buscar informações necessárias",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno ao criar o pedido",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class)
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<ApiResponseDTO<CompleteOrderDTO>> createOrder(@RequestBody NewOrderDTO newOrderDTO) {
        var response = publicController.createOrder(newOrderDTO);

        return ResponseEntity
            .status(201)
            .body(
                ApiResponseDTO.send(201,"Pedido criado com sucesso", response)
            );
    }
}
