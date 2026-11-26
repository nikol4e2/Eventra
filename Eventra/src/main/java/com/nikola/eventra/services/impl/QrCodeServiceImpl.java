package com.nikola.eventra.services.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.nikola.eventra.domain.enums.QrCodeStatusEnum;
import com.nikola.eventra.domain.models.QrCode;
import com.nikola.eventra.domain.models.Ticket;
import com.nikola.eventra.exceptions.QrCodeNotFoundException;
import com.nikola.eventra.exceptions.QrQenerationException;
import com.nikola.eventra.repositories.QrCodeRepository;
import com.nikola.eventra.services.QrCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class QrCodeServiceImpl implements QrCodeService {

    private static final int QR_HEIGHT = 300;
    private static final int QR_WIDTH = 300;

    private final QRCodeWriter qrCodeWriter;
    private final QrCodeRepository qrCodeRepository;




    @Override
    public QrCode generateQrCode(Ticket ticket) {
        try {
            UUID uniqueId = UUID.randomUUID();


            String qrCodeImage = generateQrCodeImage(uniqueId);

            QrCode qrCode=new QrCode();
            qrCode.setId(uniqueId);
            qrCode.setStatus(QrCodeStatusEnum.ACTIVE);
            qrCode.setValue(qrCodeImage);
            qrCode.setTicket(ticket);

            return qrCodeRepository.saveAndFlush(qrCode);
        }catch (WriterException | IOException e) {
            throw new QrQenerationException("Unable to generate QR code",e);
        }

    }

    private String generateQrCodeImage(UUID uniqueId) throws WriterException, IOException {
        BitMatrix bitMatrix=qrCodeWriter.encode(uniqueId.toString(),
                BarcodeFormat.QR_CODE,
                QR_WIDTH,
                QR_HEIGHT);

        BufferedImage qrCodeImage=MatrixToImageWriter.toBufferedImage(bitMatrix);

        try (
                ByteArrayOutputStream baos= new ByteArrayOutputStream())
        {
            ImageIO.write(qrCodeImage,"PNG", baos);
            byte[] imageBytes=baos.toByteArray();

            return Base64.getEncoder().encodeToString(imageBytes);
        }

    }


    @Override
    public byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId) {
        QrCode qrCode=qrCodeRepository.findByTicketIdAndTicketPurchaserId(ticketId,userId)
                .orElseThrow(QrCodeNotFoundException::new);

        try {
            return Base64.getDecoder().decode(qrCode.getValue());
        }catch (IllegalArgumentException e) {
            log.error("Invalid base64 Qr Code for ticketId: {}",ticketId,e);
            throw new QrCodeNotFoundException();
        }
    }
}
