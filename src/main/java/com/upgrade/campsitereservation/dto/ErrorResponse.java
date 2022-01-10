package com.upgrade.campsitereservation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private String action;
    @JsonProperty("error_message")
    private String errorMessage;
}
