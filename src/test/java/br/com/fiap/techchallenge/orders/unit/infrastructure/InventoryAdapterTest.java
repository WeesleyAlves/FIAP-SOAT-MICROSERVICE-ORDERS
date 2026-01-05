package br.com.fiap.techchallenge.orders.unit.infrastructure;

import br.com.fiap.techchallenge.orders.infrastructure.adapters.InventoryAdapter;
import br.com.fiap.techchallenge.orders.infrastructure.dtos.UpdateInventoryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InventoryAdapterTest {

    private HttpClient httpClient;
    private InventoryAdapter adapter;
    private final String envUrl = "http://api-estoque-env.com/v1/patch";

    @BeforeEach
    void setUp() {
        httpClient = mock(HttpClient.class);
        adapter = new InventoryAdapter(httpClient);

        ReflectionTestUtils.setField(adapter, "inventoryUpdateUrl", envUrl);
    }

    @Test
    @DisplayName("Deve disparar requisição PATCH com sucesso quando os dados forem válidos")
    void shouldSendPatchRequestSuccessfully() {
        // GIVEN
        var productId = UUID.randomUUID();
        var updateDto = new UpdateInventoryDTO(productId, 10);
        var dtoList = List.of(updateDto);

        // Criamos o mock da resposta
        HttpResponse<Void> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(204);

        // Criamos o CompletableFuture para o retorno do sendAsync
        CompletableFuture<HttpResponse<Void>> future = CompletableFuture.completedFuture(mockResponse);

        // Usamos doReturn para evitar erro de compilação com Generics do HttpClient
        doReturn(future).when(httpClient).sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.discarding().getClass()));

        // WHEN
        adapter.updateInventory(dtoList);

        // THEN
        ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
        verify(httpClient).sendAsync(requestCaptor.capture(), any());

        HttpRequest capturedRequest = requestCaptor.getValue();

        assertEquals("PATCH", capturedRequest.method());
        assertEquals(envUrl, capturedRequest.uri().toString());
        assertEquals("application/json", capturedRequest.headers().firstValue("Content-Type").orElse(""));
        assertTrue(capturedRequest.bodyPublisher().isPresent(), "O corpo da requisição deve estar presente");
    }

    @Test
    @DisplayName("Não deve disparar requisição se a lista de atualização estiver vazia ou nula")
    void shouldNotSendRequestWhenInputIsInvalid() {
        // Teste com nulo
        adapter.updateInventory(null);
        // Teste com lista vazia
        adapter.updateInventory(List.of());

        verify(httpClient, never()).sendAsync(any(), any());
    }

    @Test
    @DisplayName("Não deve disparar requisição se a URL (da env) não estiver configurada")
    void shouldNotSendRequestWhenUrlIsMissing() {
        ReflectionTestUtils.setField(adapter, "inventoryUpdateUrl", null);

        adapter.updateInventory(List.of(new UpdateInventoryDTO(UUID.randomUUID(), 1)));

        verify(httpClient, never()).sendAsync(any(), any());
    }

    @Test
    @DisplayName("Deve tratar exceção assíncrona sem interromper a execução")
    void shouldHandleAsyncExceptionGracefully() {
        // GIVEN
        var dtoList = List.of(new UpdateInventoryDTO(UUID.randomUUID(), 5));

        CompletableFuture<HttpResponse<Void>> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("Falha de rede simulada"));

        doReturn(failedFuture).when(httpClient).sendAsync(any(), any());

        // WHEN & THEN
        assertDoesNotThrow(() -> adapter.updateInventory(dtoList));
        verify(httpClient).sendAsync(any(), any());
    }

    @Test
    @DisplayName("Deve validar comportamento quando a API retorna erro (4xx ou 5xx)")
    void shouldLogWhenApiResponseIsError() {
        // GIVEN
        var dtoList = List.of(new UpdateInventoryDTO(UUID.randomUUID(), 1));

        HttpResponse<Void> errorResponse = mock(HttpResponse.class);
        when(errorResponse.statusCode()).thenReturn(400); // Bad Request

        CompletableFuture<HttpResponse<Void>> future = CompletableFuture.completedFuture(errorResponse);
        doReturn(future).when(httpClient).sendAsync(any(), any());

        // WHEN
        adapter.updateInventory(dtoList);

        // THEN
        verify(httpClient).sendAsync(any(), any());
    }
}