package com.nikola.eventra.controllers;

import com.nikola.eventra.domain.CreateEventRequest;
import com.nikola.eventra.domain.dtos.CreateEventRequestDto;
import com.nikola.eventra.domain.dtos.CreateEventResponseDto;
import com.nikola.eventra.domain.models.Event;
import com.nikola.eventra.mappers.EventMapper;
import com.nikola.eventra.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventMapper eventMapper;
    private final EventService eventService;


    @PostMapping()
    public ResponseEntity<CreateEventResponseDto> crateEvent(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CreateEventRequestDto eventRequestDto){
        CreateEventRequest createEventRequest=eventMapper.fromDto(eventRequestDto);

        UUID userId= UUID.fromString(jwt.getSubject());
        Event createdEvent=eventService.crateEvent(userId, createEventRequest);

        CreateEventResponseDto createEventResponseDto=eventMapper.toDto(createdEvent);
        return new ResponseEntity<>(createEventResponseDto, HttpStatus.CREATED);

    }

}
