package com.nikola.eventra.services.impl;

import com.nikola.eventra.domain.enums.TicketStatusEnum;
import com.nikola.eventra.domain.models.Ticket;
import com.nikola.eventra.domain.models.TicketType;
import com.nikola.eventra.domain.models.User;
import com.nikola.eventra.exceptions.TicketTypeNotFoundException;
import com.nikola.eventra.exceptions.TicketsSoldOutException;
import com.nikola.eventra.exceptions.UserNotFoundException;
import com.nikola.eventra.repositories.TicketRepository;
import com.nikola.eventra.repositories.TicketTypeRepository;
import com.nikola.eventra.repositories.UserRepository;
import com.nikola.eventra.services.QrCodeService;
import com.nikola.eventra.services.TicketTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {

    private final UserRepository userRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketRepository ticketRepository;
    private final QrCodeService qrCodeService;



    @Override
    @Transactional
    public Ticket purchaseTicket(UUID userId, UUID ticketTypeId) {
        User user=userRepository.findById(userId).orElseThrow(()->new UserNotFoundException(String.format("User with id '%s' not found", userId)));

        TicketType ticketType=ticketTypeRepository.findById(ticketTypeId).orElseThrow(()->new TicketTypeNotFoundException(String.format("TicketType with id '%s' not found", ticketTypeId)));

        int purchasedTickets=ticketRepository.countByTicketTypeId(ticketType.getId());
        Integer totalAvailable=ticketType.getTotalAvailable();

        if(purchasedTickets+1>totalAvailable)
        {
            throw new TicketsSoldOutException();
        }

        Ticket ticket= new Ticket();
        ticket.setStatus(TicketStatusEnum.PURCHASED);
        ticket.setTicketType(ticketType);
        ticket.setPurchaser(user);

        Ticket savedTicket=ticketRepository.save(ticket);
        qrCodeService.generateQrCode(savedTicket);

        return ticketRepository.save(savedTicket);
    }
}
