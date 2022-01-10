package com.upgrade.campsitereservation.service;

import com.upgrade.campsitereservation.dto.ReservationAvailabilityResponse;
import com.upgrade.campsitereservation.dto.ReservationRequest;
import com.upgrade.campsitereservation.entity.CampsiteReservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    String RESERVATION = "RESERVATION";
    CampsiteReservation reserve(ReservationRequest campsiteDetail);
    void cancelReservation(Long reservationId);
    ReservationAvailabilityResponse checkAvailability(LocalDate start, LocalDate end);
    List<CampsiteReservation> findAllReservationsByEmail(String email);
}
