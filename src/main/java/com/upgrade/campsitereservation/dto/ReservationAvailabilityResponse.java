package com.upgrade.campsitereservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ReservationAvailabilityResponse {
    @JsonFormat(pattern="MM/dd/yyyy")
    private LocalDate start;
    @JsonFormat(pattern="MM/dd/yyyy")
    private LocalDate end;
    boolean available;
}
