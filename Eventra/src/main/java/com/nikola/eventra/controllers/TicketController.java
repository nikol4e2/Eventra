package com.nikola.eventra.controllers;


import com.nikola.eventra.domain.dtos.ListTicketResponseDto;
import com.nikola.eventra.mappers.TicketMapper;
import com.nikola.eventra.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.nikola.eventra.util.JwtUtil.parseUserId;

@RestController
@RequestMapping("api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;


    @GetMapping
    public Page<ListTicketResponseDto> listTickets(@AuthenticationPrincipal Jwt jwt, Pageable pageable){

            return ticketService.listTicketsForUser(parseUserId(jwt), pageable).map(ticketMapper::toListTicketResponseDto);

    }
}
