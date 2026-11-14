package com.nikola.eventra.mappers;


import com.nikola.eventra.domain.CreateEventRequest;
import com.nikola.eventra.domain.CreateTicketTypeRequest;
import com.nikola.eventra.domain.UpdateEventRequest;
import com.nikola.eventra.domain.UpdateTicketTypeRequest;
import com.nikola.eventra.domain.dtos.*;
import com.nikola.eventra.domain.models.Event;

import com.nikola.eventra.domain.models.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);

    CreateEventRequest fromDto(CreateEventRequestDto dto);

    CreateEventResponseDto toDto(Event event);


    ListEventTicketTypeResponseDto toDto(TicketType ticketType);

    ListEventResponseDto toListEventResponseDto(Event event);

    GetEventTicketTypesResponseDto toGetEventTicketTypesResponseDto(TicketType ticketType);
    GetEventDetailsResponseDto toGetEventDetailsResponseDto(Event event);

    UpdateTicketTypeRequest fromDto(UpdateTicketTypeRequestDto dto);
    UpdateEventRequest fromDto(UpdateEventRequestDto dto);

    UpdateTicketTypeResponseDto toUpdateTicketTypeResponseDto(TicketType ticketType);
    UpdateEventResponseDto toUpdateEventResponseDto(Event event);



}
