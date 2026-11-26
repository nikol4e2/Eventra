package com.nikola.eventra.controllers;

import com.nikola.eventra.domain.dtos.TicketValidationRequestDto;
import com.nikola.eventra.domain.dtos.TicketValidationResponseDto;
import com.nikola.eventra.domain.enums.TicketValidationmethodEnum;
import com.nikola.eventra.domain.models.TicketValidation;
import com.nikola.eventra.mappers.TicketValidationMapper;
import com.nikola.eventra.services.TicketValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/ticket-validations")
@RequiredArgsConstructor
public class TicketValidationController {

    private final TicketValidationService ticketValidationService;
    private final TicketValidationMapper ticketValidationMapper;



    @PostMapping
    public ResponseEntity<TicketValidationResponseDto> validateTicket(@RequestBody TicketValidationRequestDto ticketValidationRequestDto) {

        TicketValidationmethodEnum method= ticketValidationRequestDto.getMethod();
        TicketValidation ticketValidation;
        if (TicketValidationmethodEnum.MANUAL.equals(method)) {
            ticketValidation=ticketValidationService.validateTicketManually(ticketValidationRequestDto.getId());
        }else {
            ticketValidation=ticketValidationService.validateTicketByQrCode(ticketValidationRequestDto.getId());
        }

        return ResponseEntity.ok(ticketValidationMapper.toTicketValidationResponseDto(ticketValidation));

    }




}
