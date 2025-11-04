package br.com.fiap.techchallenge.orders.api.handlers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderHandler {
    public OrderHandler() {}

    @GetMapping("/")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello World");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable String id) {
        return ResponseEntity.ok(id);
    }

    @GetMapping("/queue")
    public ResponseEntity<String> listQueue() {
        return ResponseEntity.ok("{\n" +
                "\t\"status\": 200,\n" +
                "\t\"message\": \"Fila pública listada com sucesso\",\n" +
                "\t\"data\": [\n" +
                "\t\t{\n" +
                "\t\t\t\"id\": \"10ca5645-26e0-4dda-bb3c-11a00009774b\",\n" +
                "\t\t\t\"order_number\": 4,\n" +
                "\t\t\t\"status\": \"Recebido\",\n" +
                "\t\t\t\"customer_id\": \"8c6e378f-9b3c-4e97-9fbe-ffbfb659d15f\",\n" +
                "\t\t\t\"notes\": \"Com cobertura de morango\",\n" +
                "\t\t\t\"price\": 14.40,\n" +
                "\t\t\t\"created_at\": \"2025-11-03T20:14:24.32921\",\n" +
                "\t\t\t\"updated_at\": \"2025-11-03T20:15:12.156228\",\n" +
                "\t\t\t\"products\": [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"id\": \"f809b1c5-6f70-8192-d345-6789012345f0\",\n" +
                "\t\t\t\t\t\"name\": \"Casquinha de Baunilha\",\n" +
                "\t\t\t\t\t\"quantity\": 1,\n" +
                "\t\t\t\t\t\"price\": 4.50,\n" +
                "\t\t\t\t\t\"totalValue\": 4.50\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"id\": \"e7f9a0b4-5e6f-7081-c234-5678901234ef\",\n" +
                "\t\t\t\t\t\"name\": \"Sundae Chocolate\",\n" +
                "\t\t\t\t\t\"quantity\": 1,\n" +
                "\t\t\t\t\t\"price\": 9.90,\n" +
                "\t\t\t\t\t\"totalValue\": 9.90\n" +
                "\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}");
    }

}
