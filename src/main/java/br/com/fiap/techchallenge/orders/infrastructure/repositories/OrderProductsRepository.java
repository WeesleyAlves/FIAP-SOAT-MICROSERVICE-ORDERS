package br.com.fiap.techchallenge.orders.infrastructure.repositories;


import br.com.fiap.techchallenge.orders.infrastructure.entities.OrderProductIdJPA;
import br.com.fiap.techchallenge.orders.infrastructure.entities.OrderProductsEntityJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderProductsRepository extends JpaRepository<OrderProductsEntityJPA, OrderProductIdJPA> {
    List<OrderProductsEntityJPA> findAllById_OrderId(UUID orderId);
}
