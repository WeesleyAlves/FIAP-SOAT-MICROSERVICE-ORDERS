package br.com.fiap.techchallenge.orders.infrastructure.datasources;

import br.com.fiap.techchallenge.orders.infrastructure.dtos.UpdateInventoryDTO;

import java.util.List;

public interface InventoryDatasource {
    void updateInventory(List<UpdateInventoryDTO> updateInventoryDTO);
}
