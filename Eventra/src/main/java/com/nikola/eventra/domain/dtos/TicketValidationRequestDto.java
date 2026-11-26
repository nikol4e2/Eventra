package com.nikola.eventra.domain.dtos;

import com.nikola.eventra.domain.enums.TicketValidationmethodEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TicketValidationRequestDto {

    private UUID id;
    private TicketValidationmethodEnum method;


}
