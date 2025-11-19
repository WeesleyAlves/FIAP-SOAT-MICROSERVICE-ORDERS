package br.com.fiap.techchallenge.orders.unit.infrastructure;

import br.com.fiap.techchallenge.orders.application.dtos.in.ProductInDTO;
import br.com.fiap.techchallenge.orders.infrastructure.adapters.ProductsAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductsAdapterTest {

    private ProductsAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ProductsAdapter();
    }

    // -------------------------------------------------------------------------
    // Testes para getAllByIds(List<UUID>)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve retornar a lista de produtos mockada, independentemente dos IDs de entrada")
    void shouldReturnMockedProductsRegardlessOfInputIds() {
        List<UUID> inputIds = List.of(UUID.randomUUID(), UUID.randomUUID());

        List<ProductInDTO> result = adapter.getAllByIds(inputIds);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());

        ProductInDTO product1 = result.get(0);
        assertEquals(UUID.fromString("f809b1c5-6f70-8192-d345-6789012345f0"), product1.id());
        assertEquals("Casquinha de morango", product1.name());
        assertEquals(BigDecimal.valueOf(4.99), product1.price());
    }

    @Test
    @DisplayName("Deve retornar a lista de produtos mockada mesmo com lista de IDs vazia")
    void shouldReturnMockedProductsWhenInputListIsEmpty() {
        List<UUID> emptyIds = Collections.emptyList();

        List<ProductInDTO> result = adapter.getAllByIds(emptyIds);

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}