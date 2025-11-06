package br.com.fiap.techchallenge.orders.application.gateways;

import br.com.fiap.techchallenge.orders.infrastructure.datasources.InventoryDatasource;

public class InventoryGateway {
    private final InventoryDatasource inventoryDatasource;

    public InventoryGateway(InventoryDatasource inventoryDatasource) {
        this.inventoryDatasource = inventoryDatasource;
    }

    public void updateInventory() {
        inventoryDatasource.updateInventory();
    }
}
