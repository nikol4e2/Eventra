package com.nikola.eventra.services.impl;

import com.nikola.eventra.domain.CreateEventRequest;
import com.nikola.eventra.domain.UpdateEventRequest;
import com.nikola.eventra.domain.UpdateTicketTypeRequest;
import com.nikola.eventra.domain.models.Event;
import com.nikola.eventra.domain.models.TicketType;
import com.nikola.eventra.domain.models.User;
import com.nikola.eventra.exceptions.EventNotFoundException;
import com.nikola.eventra.exceptions.EventUpdateException;
import com.nikola.eventra.exceptions.TicketTypeNotFoundException;
import com.nikola.eventra.exceptions.UserNotFoundException;
import com.nikola.eventra.repositories.EventRepository;
import com.nikola.eventra.repositories.UserRepository;
import com.nikola.eventra.services.EventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Override
    public Optional<Event> getEventForOrganizer(UUID organizerId, UUID id) {
        return eventRepository.findByIdAndOrganizerId(id,organizerId);
    }

    @Override
    @Transactional
    public Event updateEvent(UUID organizerId, UUID eventId, UpdateEventRequest event) {
        if(event.getId()==null)
        {
            throw new EventUpdateException("Event id cannot be null");
        }
        if(!eventId.equals(event.getId()))
        {
            throw new EventUpdateException("Cannot update the ID of an event");
        }

        Event existingEvent=eventRepository.findByIdAndOrganizerId(eventId,organizerId).orElseThrow(()-> new EventNotFoundException(String.format("Event with ID '%s' does not exist",eventId )));

        existingEvent.setName(event.getName());
        existingEvent.setStart(event.getStart());
        existingEvent.setEnd(event.getEnd());
        existingEvent.setVenue(event.getVenue());
        existingEvent.setSalesStart(event.getSalesStart());
        existingEvent.setSalesEnd(event.getSalesEnd());
        existingEvent.setStatus(event.getStatus());

        Set<UUID> requestTicketTypeIds=event.getTicketTypes()
                .stream()
                .map(UpdateTicketTypeRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingEvent.getTicketTypes().removeIf(existingTicketType -> !requestTicketTypeIds.contains(existingTicketType.getId()));

        Map<UUID,TicketType> existingTicketTypesIndex=existingEvent.getTicketTypes().stream()
                .collect(Collectors.toMap(TicketType::getId, Function.identity()));

        for(UpdateTicketTypeRequest requestTicketType:event.getTicketTypes())
        {
            if(requestTicketType.getId()==null)
            {
                TicketType ticketTypeToCreate=new TicketType();
                ticketTypeToCreate.setName(requestTicketType.getName());
                ticketTypeToCreate.setPrice(requestTicketType.getPrice());
                ticketTypeToCreate.setDescription(requestTicketType.getDescription());
                ticketTypeToCreate.setTotalAvailable(requestTicketType.getTotalAvailable());
                ticketTypeToCreate.setEvent(existingEvent);
                existingEvent.getTicketTypes().add(ticketTypeToCreate);
                existingEvent.getTicketTypes().add(ticketTypeToCreate);

            }else if(existingTicketTypesIndex.containsKey(requestTicketType.getId()))
            {
               TicketType existingTicketType=existingTicketTypesIndex.get(requestTicketType.getId());
               existingTicketType.setName(requestTicketType.getName());
               existingTicketType.setPrice(requestTicketType.getPrice());
               existingTicketType.setDescription(requestTicketType.getDescription());
               existingTicketType.setTotalAvailable(requestTicketType.getTotalAvailable());
            }else throw new TicketTypeNotFoundException(String.format("TicketType with ID '%s' does not exist",requestTicketType.getId()));
        }
        return eventRepository.save(existingEvent);

    }
}
