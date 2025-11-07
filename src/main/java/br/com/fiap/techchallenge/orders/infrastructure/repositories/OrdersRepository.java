package br.com.fiap.techchallenge.orders.infrastructure.repositories;

import br.com.fiap.techchallenge.orders.infrastructure.entities.OrderEntityJPA;
import br.com.fiap.techchallenge.orders.utils.constants.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrdersRepository extends JpaRepository<OrderEntityJPA, UUID> {

    @Query("SELECT o FROM orders o JOIN FETCH o.products WHERE o.status IN :statusIn")
    List<OrderEntityJPA> findByStatusInWithProducts(@Param("statusIn") List<OrderStatus> statusIn);

}
