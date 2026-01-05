package br.com.fiap.techchallenge.orders.infrastructure.adapters;

import br.com.fiap.techchallenge.orders.infrastructure.datasources.InventoryDatasource;
import br.com.fiap.techchallenge.orders.infrastructure.dtos.UpdateInventoryDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Slf4j
@Service
public class InventoryAdapter implements InventoryDatasource {

    @Value("${external.ms.inventory.updateInventory}")
    private String inventoryUpdateUrl;

    private final HttpClient httpClient;
    private final ObjectWriter jsonWriter;

    public InventoryAdapter() {
        this.httpClient = HttpClient.newHttpClient();
        this.jsonWriter = new ObjectMapper().writer();
    }

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
                    // Alteração principal: .method("PATCH", ...) em vez de .POST()
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(bodyJson))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.discarding())
                    .thenAccept(response -> {
                        if (response.statusCode() >= 400) {
                            log.error("Erro na requisição PATCH: {}", response.statusCode());
                        }
                    })
                    .exceptionally(ex -> {
                        log.error("Falha ao comunicar com o serviço de inventário", ex);
                        return null;
                    });

        } catch (Exception e) {
            log.error("Falha ao comunicar com o serviço de inventário", e);
        }
    }
}