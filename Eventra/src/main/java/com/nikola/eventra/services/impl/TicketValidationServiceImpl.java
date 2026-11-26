package com.nikola.eventra.services.impl;

import com.nikola.eventra.domain.enums.QrCodeStatusEnum;
import com.nikola.eventra.domain.enums.TicketValidationStatusEnum;
import com.nikola.eventra.domain.enums.TicketValidationmethodEnum;
import com.nikola.eventra.domain.models.QrCode;
import com.nikola.eventra.domain.models.Ticket;
import com.nikola.eventra.domain.models.TicketValidation;
import com.nikola.eventra.exceptions.QrCodeNotFoundException;
import com.nikola.eventra.exceptions.TicketNotFoundException;
import com.nikola.eventra.repositories.QrCodeRepository;
import com.nikola.eventra.repositories.TicketRepository;
import com.nikola.eventra.repositories.TicketValidationRepository;
import com.nikola.eventra.services.TicketValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketValidationServiceImpl implements TicketValidationService {

    private final QrCodeRepository qrCodeRepository;
    private final TicketValidationRepository ticketValidationRepository;
    private final TicketRepository ticketRepository;



    @Override
    public TicketValidation validateTicketByQrCode(UUID qrCodeId) {
        QrCode qrCode=qrCodeRepository.findByIdAndStatus(qrCodeId, QrCodeStatusEnum.ACTIVE)
                .orElseThrow(() -> new QrCodeNotFoundException(String.format("QR Code with ID %s was not found",qrCodeId)));


        Ticket ticket=qrCode.getTicket();
        return validateTicket(ticket);
    }

    private TicketValidation validateTicket(Ticket ticket) {
        TicketValidation ticketValidation=new TicketValidation();
        ticketValidation.setTicket(ticket);
        ticketValidation.setValidationMethod(TicketValidationmethodEnum.QR_SCAN);


        TicketValidationStatusEnum ticketValidationStatusEnum= ticket.getValidations().stream()
                .filter(v-> TicketValidationStatusEnum.VALID.equals(v.getStatus()))
                .findFirst()
                .map(v->TicketValidationStatusEnum.INVALID)
                .orElse(TicketValidationStatusEnum.VALID);


        ticketValidation.setStatus(ticketValidationStatusEnum);

        return ticketValidationRepository.save(ticketValidation);
    }

    @Override
    public TicketValidation validateTicketManually(UUID ticketId) {
        Ticket ticket=ticketRepository.findById(ticketId)
                .orElseThrow(()->new TicketNotFoundException(String.format("Ticket with ID %s was not found",ticketId)));

        return validateTicket(ticket);



    }
}
