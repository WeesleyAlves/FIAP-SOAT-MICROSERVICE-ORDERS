package br.com.fiap.techchallenge.orders.application.gateways;

import br.com.fiap.techchallenge.orders.infrastructure.datasources.InventoryDatasource;
import br.com.fiap.techchallenge.orders.infrastructure.dtos.UpdateInventoryDTO;

import java.util.List;

public class InventoryGateway {
    private final InventoryDatasource inventoryDatasource;

    public InventoryGateway(InventoryDatasource inventoryDatasource) {
        this.inventoryDatasource = inventoryDatasource;
    }

    public void updateInventory(List<UpdateInventoryDTO> updateInventoryDTO) {
        inventoryDatasource.updateInventory(updateInventoryDTO);
    }
}
