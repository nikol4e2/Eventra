package com.nikola.eventra.mappers;

import com.nikola.eventra.domain.dtos.TicketValidationRequestDto;
import com.nikola.eventra.domain.dtos.TicketValidationResponseDto;
import com.nikola.eventra.domain.models.TicketValidation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketValidationMapper {


    @Mapping(target = "ticketId", source = "ticket.id")
    TicketValidationResponseDto toTicketValidationResponseDto(TicketValidation ticketValidation);


}
