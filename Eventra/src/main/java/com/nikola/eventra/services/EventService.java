package com.nikola.eventra.services;

import com.nikola.eventra.domain.CreateEventRequest;
import com.nikola.eventra.domain.models.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface EventService {

    Event crateEvent(UUID organizerId, CreateEventRequest event);
    Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable);


}
