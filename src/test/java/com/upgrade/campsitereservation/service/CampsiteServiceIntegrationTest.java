package com.upgrade.campsitereservation.service;

import com.upgrade.campsitereservation.CampsiteReservationApplication;
import com.upgrade.campsitereservation.dto.ReservationRequest;
import com.upgrade.campsitereservation.entity.CampsiteReservation;
import com.upgrade.campsitereservation.errorhandler.CampsiteException;
import com.upgrade.campsitereservation.repo.CampsiteReservationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        classes = CampsiteReservationApplication.class)
@ExtendWith(SpringExtension.class)
class CampsiteServiceIntegrationTest {
    @Autowired
    private CampsiteReservationRepository campsiteReservationRepository;

    @Autowired
    private CampsiteService underTest;

    @AfterEach
    void tearDown() {
        campsiteReservationRepository.deleteAll();
    }

    @Test
    void givenValidReserveRequestOnReserveShouldReserveCampsite() {
        LocalDate now = LocalDate.now();
        ReservationRequest reservationRequest = createReservationRequest(now.plusDays(10), 2, "aaa@domain.io");

        CampsiteReservation savedReservation = underTest.reserve(reservationRequest);

        Optional<CampsiteReservation> reservationInDBOptional = campsiteReservationRepository.findById(savedReservation.getId());
        assertTrue(reservationInDBOptional.isPresent());

        CampsiteReservation reservationInDB = reservationInDBOptional.get();
        assertEquals(reservationRequest.getArrival(), reservationInDB.getArrival());
        assertEquals(reservationRequest.calculateDeparture(), reservationInDB.getDeparture());
        assertEquals(reservationRequest.getEmail(), reservationInDB.getReservedByEmail());
    }

    @Test
    void givenReservationNotAvailableOnReserveShouldReserveCampsite() {
        LocalDate now = LocalDate.now();
        CampsiteReservation campsiteReservation = createCampsiteReservation(now);
        campsiteReservationRepository.save(campsiteReservation);
        ReservationRequest reservationRequest = createReservationRequest(now.plusDays(4), 3, "otheremail@otherdomain.com");

        Assertions.assertThrows(CampsiteException.class, () -> underTest.reserve(reservationRequest));
    }

    //todo: add other integration tests - availability, cancel, checkin, checkout etc

    private CampsiteReservation createCampsiteReservation(LocalDate now) {
        CampsiteReservation campsiteReservation = new CampsiteReservation();
        campsiteReservation.setArrival(now.plusDays(5));
        campsiteReservation.setDeparture(now.plusDays(7));
        campsiteReservation.setReservedOn(LocalDateTime.now());
        campsiteReservation.setReservedByEmail("somemail@somedomain.com");
        return campsiteReservation;
    }

    private ReservationRequest createReservationRequest(LocalDate arrival, int numberOfDays, String email) {
        ReservationRequest reservationRequest = new ReservationRequest();
        reservationRequest.setEmail(email);
        reservationRequest.setArrival(arrival);
        reservationRequest.setNumberOfDays(numberOfDays);
        return reservationRequest;
    }
}