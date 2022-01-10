package com.upgrade.campsitereservation.service;

import com.upgrade.campsitereservation.dto.ReservationRequest;
import com.upgrade.campsitereservation.entity.CampsiteReservation;
import com.upgrade.campsitereservation.errorhandler.CampsiteException;
import com.upgrade.campsitereservation.repo.CampsiteReservationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CampsiteServiceUnitTest {
    @Mock
    private CampsiteReservationRepository campsiteReservationRepository;

    @InjectMocks
    private CampsiteService underTest;

    @Test
    void givenValidReserveRequestOnReserveShouldReserveCampsite() {
        ReservationRequest reservationRequest = createReservationRequest(10);
        when(campsiteReservationRepository.countReservationsBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(0L);

        underTest.reserve(reservationRequest);

        ArgumentCaptor<CampsiteReservation> reservationArgumentCaptor = ArgumentCaptor.forClass(CampsiteReservation.class);
        Mockito.verify(campsiteReservationRepository).save(reservationArgumentCaptor.capture());
        CampsiteReservation reservation = reservationArgumentCaptor.getValue();
        assertNotNull(reservation);
        assertEquals(reservationRequest.getArrival(), reservation.getArrival());
        assertEquals(reservationRequest.calculateDeparture(), reservation.getDeparture());
        assertEquals(reservationRequest.getEmail(), reservation.getReservedByEmail());
    }

    @Test
    void givenInvalidReserveRequest40DaysInAdvanceOnReserveShouldThrowException() {
        ReservationRequest reservationRequest = createReservationRequest(40);

        Assertions.assertThrows(CampsiteException.class, () -> underTest.reserve(reservationRequest));
    }

    @Test
    void givenValidReserveRequestButReservationNotAvailableOnReserveShouldThrowException() {
        ReservationRequest reservationRequest = createReservationRequest(10);
        when(campsiteReservationRepository.countReservationsBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(1L);

        Assertions.assertThrows(CampsiteException.class, () -> underTest.reserve(reservationRequest));
    }

    //todo: unit tests other methods - checkin, checkout, availability etc

    private ReservationRequest createReservationRequest(int daysInAdvance) {
        LocalDate now = LocalDate.now();
        LocalDate arrival = now.plusDays(daysInAdvance);
        ReservationRequest reservationRequest = new ReservationRequest();
        reservationRequest.setEmail("abc@gmail.com");
        reservationRequest.setArrival(arrival);
        reservationRequest.setNumberOfDays(3);
        return reservationRequest;
    }
}