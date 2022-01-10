package com.upgrade.campsitereservation.service;

import com.upgrade.campsitereservation.dto.CheckInOutRequest;
import com.upgrade.campsitereservation.dto.ReservationAvailabilityResponse;
import com.upgrade.campsitereservation.dto.ReservationRequest;
import com.upgrade.campsitereservation.entity.CampsiteReservation;
import com.upgrade.campsitereservation.errorhandler.CampsiteException;
import com.upgrade.campsitereservation.repo.CampsiteReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class CampsiteService implements CheckInOutService, ReservationService {

    @Autowired
    private CampsiteReservationRepository campsiteReservationRepository;

    @Override
    public ReservationAvailabilityResponse checkAvailability(LocalDate start, LocalDate end) {
        LocalDate endDate = Objects.isNull(end)
                ? start.plusDays(30)
                : end;

        long totalNumberOfReservations = campsiteReservationRepository.countReservationsBetween(start, endDate);
        log.info("Found {} reservations between [{} -{}]", totalNumberOfReservations, start, endDate);
        return ReservationAvailabilityResponse.builder()
                .start(start)
                .end(endDate)
                .available(totalNumberOfReservations == 0)
                .build();
    }


    @Override
    public void checkIn(CheckInOutRequest checkInOutRequest) {
        Optional<CampsiteReservation> reservationOptional = campsiteReservationRepository.findById(checkInOutRequest.getReservationId());
        if (!reservationOptional.isPresent()) {
            throw new CampsiteException(CHECK_IN, String.format("Failed to check in, reservation %d doesn't exist", checkInOutRequest.getReservationId()));
        }

        CampsiteReservation campsiteReservation = reservationOptional.get();
        LocalDateTime checkInTime = Objects.isNull(checkInOutRequest.getEventDateTime())
                ? LocalDateTime.now()
                : checkInOutRequest.getEventDateTime();
        if (!Objects.equals(campsiteReservation.getArrival(), checkInTime.toLocalDate())) {
            throw new CampsiteException(CHECK_IN, "Failed to check in, must check in on arrival date");
        }

        campsiteReservation.setCheckIn(checkInTime);
        campsiteReservationRepository.save(campsiteReservation);
    }

    @Override
    public void checkOut(CheckInOutRequest checkInOutRequest) {
        Optional<CampsiteReservation> reservationOptional = campsiteReservationRepository.findById(checkInOutRequest.getReservationId());
        if (!reservationOptional.isPresent()) {
            throw new CampsiteException(CHECK_OUT, (String.format("Failed to check in, reservation %d doesn't exist", checkInOutRequest.getReservationId())));
        }
        CampsiteReservation campsiteReservation = reservationOptional.get();
        LocalDateTime checkOutTime = Objects.isNull(checkInOutRequest.getEventDateTime())
                ? LocalDateTime.now()
                : checkInOutRequest.getEventDateTime();
        if (Objects.isNull(campsiteReservation.getCheckIn())) {
            throw new CampsiteException(CHECK_OUT, "Failed to check out, must check in before check out");
        }

        if (!Objects.equals(campsiteReservation.getDeparture(), checkOutTime.toLocalDate())) {
            throw new CampsiteException(CHECK_OUT, "Failed to check out, please check out on departure date or modify the reservation");
        }

        campsiteReservation.setCheckOut(checkOutTime);
        campsiteReservationRepository.save(campsiteReservation);
    }

    @Override
    public void cancelReservation(Long reservationId) {
        Optional<CampsiteReservation> campsiteReservation = campsiteReservationRepository.findById(reservationId);
        if (!campsiteReservation.isPresent()) {
            throw new CampsiteException(RESERVATION, String.format("reservation %d doesn't exist", reservationId));
        }
        campsiteReservationRepository.delete(campsiteReservation.get());
    }

    @Override
    public synchronized CampsiteReservation reserve(ReservationRequest campsiteDetail) {
        validateReservationRequest(campsiteDetail);

        CampsiteReservation reservation = CampsiteReservation.builder()
                .reservedByEmail(campsiteDetail.getEmail())
                .guests(campsiteDetail.getNumberOfDays())
                .arrival(campsiteDetail.getArrival())
                .departure(campsiteDetail.calculateDeparture())
                .reservedOn(LocalDateTime.now())
                .build();
        return campsiteReservationRepository.save(reservation);
    }

    private void validateReservationRequest(ReservationRequest campsiteDetail) {
        int numberOfDays = campsiteDetail.getNumberOfDays();
        if (numberOfDays < 1 || numberOfDays > 3) {
            throw new CampsiteException(RESERVATION, "You cannot reserve for more than 3 days");
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate arrival = campsiteDetail.getArrival();
        long daysBetweenCurrentAndArrival = ChronoUnit.DAYS.between(currentDate, arrival);
        if (daysBetweenCurrentAndArrival < 1 || daysBetweenCurrentAndArrival > 30) {
            throw new CampsiteException(RESERVATION, "Campsite can be reserved between 1 to 30 days in advance");
        }

        LocalDate departure = campsiteDetail.calculateDeparture();
        long totalNumberOfReservations = campsiteReservationRepository.countReservationsBetween(arrival, departure);

        if (totalNumberOfReservations > 0) {
            throw new CampsiteException(RESERVATION, "Campsite is not available");
        }
    }

    @Override
    public List<CampsiteReservation> findAllReservationsByEmail(String email) {
        return campsiteReservationRepository.findAllByReservedByEmail(email);
    }
}
