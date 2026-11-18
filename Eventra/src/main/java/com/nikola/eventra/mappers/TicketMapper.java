package com.nikola.eventra.mappers;

import com.nikola.eventra.domain.dtos.ListTicketResponseDto;
import com.nikola.eventra.domain.dtos.ListTicketTicketTypeDtoResponse;
import com.nikola.eventra.domain.models.Ticket;
import com.nikola.eventra.domain.models.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

    ListTicketTicketTypeDtoResponse toListTicketTicketTypeResponseDto(TicketType ticketType);

    ListTicketResponseDto toListTicketResponseDto(Ticket ticket);
}
