package com.nikola.eventra.services;

import com.nikola.eventra.domain.models.Ticket;

import java.util.UUID;

public interface TicketTypeService {

    Ticket purchaseTicket(UUID userId, UUID ticketTypeId);

}
