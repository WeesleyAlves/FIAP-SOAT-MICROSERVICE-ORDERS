package br.com.fiap.techchallenge.orders.api.handlers;

import br.com.fiap.techchallenge.orders.api.handlers.common.ApiResponseDTO;
import br.com.fiap.techchallenge.orders.application.controllers.PublicController;
import br.com.fiap.techchallenge.orders.application.dtos.in.NewOrderDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class PublicHandler {

    private final PublicController publicController;

    public PublicHandler() {
        this.publicController = new PublicController();
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
    public ResponseEntity<String> listQueue() {
        return ResponseEntity.ok("");
    }

    @PostMapping("/order")
    public ResponseEntity<ApiResponseDTO<Object>> createOrder(@RequestBody NewOrderDTO newOrderDTO) {
        var response = publicController.createOrder(newOrderDTO);

        return ResponseEntity
            .status(201)
            .body(
                ApiResponseDTO.send(201,"Pedido criado com sucesso", response)
            );
    }
}
