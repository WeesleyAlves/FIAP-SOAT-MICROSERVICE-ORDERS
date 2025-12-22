package br.com.fiap.techchallenge.orders.infrastructure.adapters;

import br.com.fiap.techchallenge.orders.infrastructure.datasources.InventoryDatasource;
import br.com.fiap.techchallenge.orders.infrastructure.dtos.UpdateInventoryDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class InventoryAdapter implements InventoryDatasource {

    @Value("${external.ms.inventory.updateInventory}")
    private String inventoryUpdateUrl;

    private final HttpClient httpClient; // Removido static final
    private final ObjectWriter jsonWriter;

    // Construtor para o Spring e para os Testes
    public InventoryAdapter() {
        this.httpClient = HttpClient.newHttpClient();
        this.jsonWriter = new ObjectMapper().writer();
    }

    // Construtor opcional para testes (ou use o de cima com Mockito)
    public InventoryAdapter(HttpClient httpClient) {
        this.httpClient = httpClient;
        this.jsonWriter = new ObjectMapper().writer();
    }

    @Override
    public void updateInventory(List<UpdateInventoryDTO> updateInventoryDTO) {
        if (updateInventoryDTO == null || updateInventoryDTO.isEmpty() ||
                inventoryUpdateUrl == null || inventoryUpdateUrl.isBlank()) {
            return;
        }

        try {
            String bodyJson = jsonWriter.writeValueAsString(updateInventoryDTO);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(inventoryUpdateUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(bodyJson))
                    .build();

            // Usando a instância injetada
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.discarding())
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        return null;
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}