package com.nikola.eventra.services.impl;

import com.nikola.eventra.domain.CreateEventRequest;
import com.nikola.eventra.domain.models.Event;
import com.nikola.eventra.domain.models.TicketType;
import com.nikola.eventra.domain.models.User;
import com.nikola.eventra.exceptions.UserNotFoundException;
import com.nikola.eventra.repositories.EventRepository;
import com.nikola.eventra.repositories.UserRepository;
import com.nikola.eventra.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class EventServiceImpl implements EventService {


    private final UserRepository userRepository;
    private final EventRepository eventRepository;


    @Override
    public Event crateEvent(UUID organizerId, CreateEventRequest event) {
        User organizer=userRepository.findById(organizerId).orElseThrow(()->new UserNotFoundException(String.format("User with id '%s' not found", organizerId)));

        Event eventToCreate= new Event();
        List<TicketType> ticketTypesToCreate=event.getTicketTypes().stream().map(
                ticketType ->{
                    TicketType ticketTypeToCreate=new TicketType();
                    ticketTypeToCreate.setName(ticketType.getName());
                    ticketTypeToCreate.setPrice(ticketType.getPrice());
                    ticketTypeToCreate.setDescription(ticketType.getDescription());
                    ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                    ticketTypeToCreate.setEvent(eventToCreate);
                    return ticketTypeToCreate;
                }
        ).toList();



        eventToCreate.setName(event.getName());
        eventToCreate.setStart(event.getStart());
        eventToCreate.setEnd(event.getEnd());
        eventToCreate.setVenue(event.getVenue());
        eventToCreate.setSalesStart(event.getSalesStart());
        eventToCreate.setSalesEnd(event.getSalesEnd());
        eventToCreate.setStatus(event.getStatus());
        eventToCreate.setOrganizer(organizer);
        eventToCreate.setTicketTypes(ticketTypesToCreate);

        return eventRepository.save(eventToCreate);



    }

    @Override
    public Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable) {
        return eventRepository.findByOrganizerId(organizerId, pageable);
    }
}
