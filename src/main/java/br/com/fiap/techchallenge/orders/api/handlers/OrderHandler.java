package br.com.fiap.techchallenge.orders.api.handlers;

import br.com.fiap.techchallenge.orders.api.handlers.common.ApiResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class OrderHandler {
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
    public ResponseEntity<ApiResponseDTO<Object>> createOrder(@RequestBody Object order) {
        return ResponseEntity
            .status(201)
            .body(
                ApiResponseDTO.success("Pedido criado com sucesso", "")
            );
    }
}
