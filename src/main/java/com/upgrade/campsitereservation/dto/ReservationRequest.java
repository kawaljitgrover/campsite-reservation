package com.upgrade.campsitereservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ReservationRequest {
    private String email;
    private String name;
    @JsonFormat(pattern="MM/dd/yyyy")
    private LocalDate arrival;
    @JsonProperty("number_days")
    private int numberOfDays;


    public LocalDate calculateDeparture() {
        return arrival.plusDays(numberOfDays);
    }
}
