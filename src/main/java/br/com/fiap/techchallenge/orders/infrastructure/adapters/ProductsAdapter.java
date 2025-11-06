package br.com.fiap.techchallenge.orders.infrastructure.adapters;

import br.com.fiap.techchallenge.orders.application.dtos.in.ProductInDTO;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.ProductsDatasource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ProductsAdapter implements ProductsDatasource {
    @Override
    public List<ProductInDTO> getAllByIds(List<UUID> ids) {
        return List.of(
            new ProductInDTO(
                UUID.fromString("f809b1c5-6f70-8192-d345-6789012345f0" ),
                "Casquinha de morango",
                BigDecimal.valueOf(4.99)
            ),
            new ProductInDTO(
                UUID.fromString("e7f9a0b4-5e6f-7081-c234-5678901234ef" ),
                "Suco de laranja",
                BigDecimal.valueOf(1.99)
            )
        );
    }
}
