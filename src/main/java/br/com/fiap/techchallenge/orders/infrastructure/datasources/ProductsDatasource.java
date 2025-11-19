package br.com.fiap.techchallenge.orders.infrastructure.datasources;

import br.com.fiap.techchallenge.orders.application.dtos.in.ProductInDTO;

import java.util.List;
import java.util.UUID;

public interface ProductsDatasource {
    List<ProductInDTO> getAllByIds(List<UUID> ids);
}
