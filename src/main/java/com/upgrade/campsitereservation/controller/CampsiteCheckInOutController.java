package com.upgrade.campsitereservation.controller;

import com.upgrade.campsitereservation.dto.CheckInOutRequest;
import com.upgrade.campsitereservation.service.CheckInOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/campsite")
public class CampsiteCheckInOutController {
    @Autowired
    private CheckInOutService checkInOutService;

    @PatchMapping("/checkin")
    public ResponseEntity<?> checkIn(@RequestBody CheckInOutRequest checkInOutRequest) {
        checkInOutService.checkIn(checkInOutRequest);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/checkout")
    public ResponseEntity<?> checkOut(@RequestBody CheckInOutRequest checkInOutRequest) {
        checkInOutService.checkOut(checkInOutRequest);
        return ResponseEntity.noContent().build();
    }

}
