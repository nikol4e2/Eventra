package com.nikola.eventra.services;


import com.nikola.eventra.domain.models.QrCode;
import com.nikola.eventra.domain.models.Ticket;

import java.util.UUID;

public interface QrCodeService {

    QrCode generateQrCode(Ticket ticket);
    byte [] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId);
}
