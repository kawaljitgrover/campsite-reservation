package com.upgrade.campsitereservation.controller;

import com.upgrade.campsitereservation.dto.ReservationRequest;
import com.upgrade.campsitereservation.entity.CampsiteReservation;
import com.upgrade.campsitereservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/campsite")
public class CampsiteReservationController {
    @Autowired
    private ReservationService reservationService;

    @PostMapping("/reserve")
    public ResponseEntity<?> reserve(@RequestBody ReservationRequest reservationRequest) {
        return ResponseEntity.ok(reservationService.reserve(reservationRequest));
    }

    @DeleteMapping("/reserve/cancel/{reservationId}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/availability")
    public ResponseEntity<?> checkAvailability(@RequestParam("start") @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate start,
                                               @RequestParam(value = "end", required = false) @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate end) {
        return ResponseEntity.ok(reservationService.checkAvailability(start, end));
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<CampsiteReservation>> findAllReservationsByEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(reservationService.findAllReservationsByEmail(email));
    }
}
