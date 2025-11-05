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

    @PatchMapping("/order/status")
    public ResponseEntity<String> updateOrderStatus(@RequestBody Object order) {
        return ResponseEntity.ok("{\n" +
                "\t\"status\": 200,\n" +
                "\t\"message\": \"Status do pedido atualizado com sucesso.\",\n" +
                "\t\"data\": {\n" +
                "\t\t\"id\": \"10ca5645-26e0-4dda-bb3c-11a00009774b\",\n" +
                "\t\t\"order_number\": 4,\n" +
                "\t\t\"status\": \"Em Preparação\",\n" +
                "\t\t\"customer_id\": \"8c6e378f-9b3c-4e97-9fbe-ffbfb659d15f\",\n" +
                "\t\t\"created_at\": \"2025-11-03T20:14:24.32921\",\n" +
                "\t\t\"updated_at\": \"2025-11-04T20:57:55.357158\"\n" +
                "\t}\n" +
        "}");
    }
}
