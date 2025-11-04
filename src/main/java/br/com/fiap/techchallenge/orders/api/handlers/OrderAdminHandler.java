package br.com.fiap.techchallenge.orders.api.handlers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/")
public class OrderAdminHandler {
    @GetMapping("/")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello World Admin");
    }

    @PostMapping("/reset-queue-number")
    public ResponseEntity<String> resetQueueNumber() {
        return ResponseEntity.ok("{\n" +
                "\t\"status\": 200,\n" +
                "\t\"message\": \"Contador reiniciado com sucesso. O próximo pedido começará em 1.\",\n" +
                "\t\"data\": \"\"\n" +
                "}");
    }
}
