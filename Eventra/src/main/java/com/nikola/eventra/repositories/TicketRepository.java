package com.nikola.eventra.repositories;

import com.nikola.eventra.domain.models.Ticket;
import com.nikola.eventra.domain.models.TicketType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {


    int countByTicketTypeId(UUID ticketTypeId);

    Page<Ticket> findByPurchaserId(UUID purchaserId, Pageable pageable);


    Optional<Ticket> findByIdAndPurchaserId(UUID uuid, UUID purchaserId);
}
