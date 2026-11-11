package com.nikola.eventra.domain.dtos;


import com.nikola.eventra.domain.enums.EventStatusEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CreateEventRequestDto {
    @NotBlank(message = "Name cannot be empty")
    private String name;

    private LocalDateTime  start;
    private LocalDateTime end;

    @NotBlank(message = "Venue cannot be empty")
    private String venue;
    private LocalDateTime salesStart;
    private LocalDateTime salesEnd;
    @NotNull(message = "Status is required")
    private EventStatusEnum status;
    @NotEmpty
    @Valid
    private List<CreateTicketTypeRequestDto> ticketTypes;

}
