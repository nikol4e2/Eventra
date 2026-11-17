package com.nikola.eventra.services;


import com.nikola.eventra.domain.models.QrCode;
import com.nikola.eventra.domain.models.Ticket;

public interface QrCodeService {

    QrCode generateQrCode(Ticket ticket);
}
