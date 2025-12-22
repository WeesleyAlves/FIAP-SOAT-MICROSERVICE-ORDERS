package br.com.fiap.techchallenge.orders.unit.infrastructure;

import br.com.fiap.techchallenge.orders.infrastructure.adapters.InventoryAdapter;
import br.com.fiap.techchallenge.orders.infrastructure.dtos.UpdateInventoryDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InventoryAdapterTest {

    // Criamos o mock do HttpClient
    private final HttpClient httpClient = mock(HttpClient.class);
    // Injetamos o mock no adapter via construtor (igual ao seu exemplo do ProductsAdapter)
    private final InventoryAdapter adapter = new InventoryAdapter(httpClient);

    @Test
    @DisplayName("deve disparar requisição async quando dados forem válidos")
    void shouldSendAsyncRequestWhenDataIsValid() {
        // Arrange
        String testUrl = "http://localhost:8080/inventory";
        ReflectionTestUtils.setField(adapter, "inventoryUpdateUrl", testUrl);
        List<UpdateInventoryDTO> dtos = List.of(new UpdateInventoryDTO(UUID.randomUUID(), 10));

        when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        // Act
        adapter.updateInventory(dtos);

        // Assert
        ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
        verify(httpClient, times(1)).sendAsync(requestCaptor.capture(), any());

        HttpRequest capturedRequest = requestCaptor.getValue();
        assertEquals(testUrl, capturedRequest.uri().toString());
        assertEquals("POST", capturedRequest.method());
    }

    @Test
    @DisplayName("não deve interagir com httpClient se a lista for vazia")
    void shouldNotInteractWhenListIsEmpty() {
        adapter.updateInventory(Collections.emptyList());
        verifyNoInteractions(httpClient);
    }

    @Test
    @DisplayName("não deve interagir com httpClient se a URL for nula")
    void shouldNotInteractWhenUrlIsNull() {
        ReflectionTestUtils.setField(adapter, "inventoryUpdateUrl", null);
        adapter.updateInventory(List.of(new UpdateInventoryDTO(UUID.randomUUID(), 1)));

        verifyNoInteractions(httpClient);
    }
}