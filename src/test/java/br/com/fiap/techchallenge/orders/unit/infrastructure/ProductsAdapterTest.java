package br.com.fiap.techchallenge.orders.unit.infrastructure;

import br.com.fiap.techchallenge.orders.application.dtos.in.ProductInDTO;
import br.com.fiap.techchallenge.orders.infrastructure.adapters.ProductsAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProductsAdapterTest {

    private final RestTemplate restTemplate = mock(RestTemplate.class);
    private final ProductsAdapter adapter = new ProductsAdapter(restTemplate);

    @Test
    @DisplayName("deve retornar lista vazia quando ids for null")
    void shouldReturnEmptyListWhenIdsNull() {
        List<ProductInDTO> result = adapter.getAllByIds(null);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deve retornar lista vazia quando ids for vazia")
    void shouldReturnEmptyListWhenIdsEmpty() {
        List<ProductInDTO> result = adapter.getAllByIds(List.of());
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deve retornar produtos quando API responder 200")
    void shouldReturnProductsWhenApiReturnsSuccess() throws Exception {
        ReflectionTestUtils.setField(adapter, "productsBaseUrl", "http://localhost/products");

        UUID id = UUID.randomUUID();

        Class<?> recordClass = Class.forName(
                "br.com.fiap.techchallenge.orders.infrastructure.adapters.ProductsAdapter$ProductResponseDTO"
        );

        Constructor<?> constructor = recordClass.getDeclaredConstructor(
                UUID.class,
                String.class,
                String.class,
                BigDecimal.class,
                boolean.class,
                String.class,
                Long.class,
                String.class,
                String.class
        );

        constructor.setAccessible(true);

        Object recordInstance = constructor.newInstance(
                id,
                "Test",
                "Desc",
                BigDecimal.TEN,
                true,
                null,
                1L,
                null,
                null
        );

        ResponseEntity<List<Object>> response =
                new ResponseEntity<>(List.of(recordInstance), HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn((ResponseEntity) response);

        List<ProductInDTO> result = adapter.getAllByIds(List.of(id));

        assertEquals(1, result.size());
        assertEquals(id, result.get(0).id());
        assertEquals("Test", result.get(0).name());
        assertEquals(BigDecimal.TEN, result.get(0).price());
    }

    @Test
    @DisplayName("deve retornar lista vazia quando API lançar exceção")
    void shouldReturnEmptyListWhenApiThrows() {
        ReflectionTestUtils.setField(adapter, "productsBaseUrl", "http://localhost/products");

        when(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException("error"));

        List<ProductInDTO> result = adapter.getAllByIds(List.of(UUID.randomUUID()));

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deve montar URL corretamente com ids")
    void shouldBuildCorrectUrl() {
        ReflectionTestUtils.setField(adapter, "productsBaseUrl", "http://baseurl/api");

        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(new ResponseEntity<>(List.of(), HttpStatus.OK));

        adapter.getAllByIds(List.of(id1, id2));

        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        verify(restTemplate).exchange(
                urlCaptor.capture(),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        );

        String capturedUrl = urlCaptor.getValue();
        assertTrue(capturedUrl.contains("ids=" + id1 + "," + id2));
        assertTrue(capturedUrl.startsWith("http://baseurl/api"));
    }
}
