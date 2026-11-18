package com.nikola.eventra.domain.dtos;


import com.nikola.eventra.domain.enums.TicketStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListTicketResponseDto {


    private UUID id;
    private TicketStatusEnum status;
    private ListTicketTicketTypeDtoResponse ticketType;
}
