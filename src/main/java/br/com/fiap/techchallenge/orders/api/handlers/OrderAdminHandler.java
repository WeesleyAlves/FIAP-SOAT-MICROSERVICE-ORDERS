package br.com.fiap.techchallenge.orders.api.handlers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
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

    @GetMapping("/orders")
    public ResponseEntity<String> getAdminOrdersList() {
        return ResponseEntity.ok("{\n" +
                "\t\"status\": 200,\n" +
                "\t\"message\": \"Pedidos listados com sucesso\",\n" +
                "\t\"data\": [\n" +
                "\t\t{\n" +
                "\t\t\t\"id\": \"10ca5645-26e0-4dda-bb3c-11a00009774b\",\n" +
                "\t\t\t\"order_number\": 4,\n" +
                "\t\t\t\"status\": \"Recebido\",\n" +
                "\t\t\t\"customer_id\": \"8c6e378f-9b3c-4e97-9fbe-ffbfb659d15f\",\n" +
                "\t\t\t\"created_at\": \"2025-11-03T20:14:24.32921\",\n" +
                "\t\t\t\"updated_at\": \"2025-11-03T20:15:12.156228\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"id\": \"5098e4f5-643e-496a-aaea-2feb7c0e6b38\",\n" +
                "\t\t\t\"order_number\": 1,\n" +
                "\t\t\t\"status\": \"Aguardando Pagamento\",\n" +
                "\t\t\t\"customer_id\": \"74c84478-80c1-7000-c952-ecf95a1e3df9\",\n" +
                "\t\t\t\"created_at\": \"2025-10-06T16:38:40.447977\",\n" +
                "\t\t\t\"updated_at\": \"2025-10-06T16:38:40.447977\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"id\": \"55ca8bfe-28d4-4885-9ee7-52f9d0d015be\",\n" +
                "\t\t\t\"order_number\": 2,\n" +
                "\t\t\t\"status\": \"Aguardando Pagamento\",\n" +
                "\t\t\t\"customer_id\": \"74c84478-80c1-7000-c952-ecf95a1e3df9\",\n" +
                "\t\t\t\"created_at\": \"2025-10-06T16:38:46.034276\",\n" +
                "\t\t\t\"updated_at\": \"2025-10-06T16:38:46.034276\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"id\": \"bdb6f1f6-641d-414e-a101-a9e2d877b19c\",\n" +
                "\t\t\t\"order_number\": 3,\n" +
                "\t\t\t\"status\": \"Aguardando Pagamento\",\n" +
                "\t\t\t\"customer_id\": \"74c84478-80c1-7000-c952-ecf95a1e3df9\",\n" +
                "\t\t\t\"created_at\": \"2025-10-06T16:39:09.492956\",\n" +
                "\t\t\t\"updated_at\": \"2025-10-06T16:39:09.492956\"\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}");
    }
}
