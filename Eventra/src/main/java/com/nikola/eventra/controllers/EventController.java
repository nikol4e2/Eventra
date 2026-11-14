package com.nikola.eventra.controllers;

import com.nikola.eventra.domain.CreateEventRequest;
import com.nikola.eventra.domain.UpdateEventRequest;
import com.nikola.eventra.domain.dtos.*;
import com.nikola.eventra.domain.models.Event;
import com.nikola.eventra.mappers.EventMapper;
import com.nikola.eventra.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

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

        UUID userId= parseUserId(jwt);
        Event createdEvent=eventService.crateEvent(userId, createEventRequest);

        CreateEventResponseDto createEventResponseDto=eventMapper.toDto(createdEvent);
        return new ResponseEntity<>(createEventResponseDto, HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<Page<ListEventResponseDto>> listEvents(@AuthenticationPrincipal Jwt jwt, Pageable pageable){
        UUID userId= parseUserId(jwt);
        Page<Event>events= eventService.listEventsForOrganizer(userId, pageable);
        return ResponseEntity.ok(events.map(eventMapper::toListEventResponseDto));
    }


    private UUID parseUserId(Jwt jwt){
        return UUID.fromString(jwt.getSubject());
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<GetEventDetailsResponseDto> getEvent(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID eventId){

        UUID userId=parseUserId(jwt);
        return eventService.getEventForOrganizer(userId, eventId)
                .map(eventMapper::toGetEventDetailsResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }


    @PutMapping("/{eventId}")
    public ResponseEntity<UpdateEventResponseDto> updateEvent(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID eventId,@Valid @RequestBody UpdateEventRequestDto upadteEventRequestDto){
        UpdateEventRequest updateEventRequest=eventMapper.fromDto(upadteEventRequestDto);

        UUID userId= parseUserId(jwt);
        Event updatedEvent=eventService.updateEvent(userId,eventId,updateEventRequest );

        UpdateEventResponseDto updateEventResponseDto=eventMapper.toUpdateEventResponseDto(updatedEvent);
        return  ResponseEntity.ok(updateEventResponseDto);

    }




}
