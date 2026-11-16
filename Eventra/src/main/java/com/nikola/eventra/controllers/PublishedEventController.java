package com.nikola.eventra.controllers;

import com.nikola.eventra.domain.dtos.ListPublishedEventResponseDto;
import com.nikola.eventra.domain.models.Event;
import com.nikola.eventra.mappers.EventMapper;
import com.nikola.eventra.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/published-events")
@RequiredArgsConstructor
public class PublishedEventController {

    private final EventMapper eventMapper;
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<Page<ListPublishedEventResponseDto>> listPublishedEvents(@RequestParam(required = false) String q, Pageable pageable){

        Page<Event> events;

        if(q!=null && !q.trim().isEmpty())
        {
            events= eventService.searchPublishedEvents(q, pageable);
        }
        events=eventService.listPublishedEvents(pageable);

        return ResponseEntity.ok(events.map(eventMapper::toListPublishedEventResponseDto));
    }


}
