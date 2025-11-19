package br.com.fiap.techchallenge.orders.unit.infrastructure;

import br.com.fiap.techchallenge.orders.infrastructure.adapters.InventoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class InventoryAdapterTest {

    private InventoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new InventoryAdapter();
    }

    // -------------------------------------------------------------------------
    // Testes para updateInventory()
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve executar updateInventory sem lançar exceção (Mocked Implementation)")
    void shouldExecuteUpdateInventoryWithoutThrowingException() {
        assertDoesNotThrow(() -> adapter.updateInventory());
    }
}