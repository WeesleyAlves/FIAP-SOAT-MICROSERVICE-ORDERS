package br.com.fiap.techchallenge.orders.infrastructure.repositories;

import br.com.fiap.techchallenge.orders.infrastructure.entities.OrderEntityJPA;
import br.com.fiap.techchallenge.orders.utils.constants.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrdersRepository extends JpaRepository<OrderEntityJPA, UUID> {
    List<OrderEntityJPA> findByStatusIn(List<OrderStatus> statusId);
}
