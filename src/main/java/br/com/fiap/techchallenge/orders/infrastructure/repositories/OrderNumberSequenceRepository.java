package br.com.fiap.techchallenge.orders.infrastructure.repositories;

import br.com.fiap.techchallenge.orders.infrastructure.entities.OrderNumberEntityJPA;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderNumberSequenceRepository extends JpaRepository<OrderNumberEntityJPA, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<OrderNumberEntityJPA> findTopByOrderByIdAsc();
}
