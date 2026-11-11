package com.nikola.eventra.mappers;


import com.nikola.eventra.domain.CreateEventRequest;
import com.nikola.eventra.domain.CreateTicketTypeRequest;
import com.nikola.eventra.domain.dtos.CreateEventRequestDto;
import com.nikola.eventra.domain.dtos.CreateEventResponseDto;
import com.nikola.eventra.domain.dtos.CreateTicketTypeRequestDto;
import com.nikola.eventra.domain.models.Event;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);

    CreateEventRequest fromDto(CreateEventRequestDto dto);

    CreateEventResponseDto toDto(Event event);

}
