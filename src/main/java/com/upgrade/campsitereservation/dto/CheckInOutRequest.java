package com.upgrade.campsitereservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CheckInOutRequest {
    @JsonProperty("reservation_id")
    private Long reservationId;
    @JsonFormat(pattern="MM/dd/yyyy HH:mm")
    @JsonProperty("event_time")
    private LocalDateTime eventDateTime;
}
