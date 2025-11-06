package br.com.fiap.techchallenge.orders.api.handlers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminHandler {
    @GetMapping("/")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("");
    }

    @PostMapping("/reset-queue-number")
    public ResponseEntity<String> resetQueueNumber() {
        return ResponseEntity.ok("");
    }

    @PatchMapping("/order/status")
    public ResponseEntity<String> updateOrderStatus(@RequestBody Object order) {
        return ResponseEntity.ok("");
    }

    @GetMapping("/orders")
    public ResponseEntity<String> getAdminOrdersList() {
        return ResponseEntity.ok("");
    }
}
