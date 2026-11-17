package com.nikola.eventra.repositories;

import com.nikola.eventra.domain.models.Ticket;
import com.nikola.eventra.domain.models.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {


    int countByTicketTypeId(UUID ticketTypeId);

}
