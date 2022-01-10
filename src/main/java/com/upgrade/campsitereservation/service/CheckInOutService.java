package com.upgrade.campsitereservation.service;


import com.upgrade.campsitereservation.dto.CheckInOutRequest;

public interface CheckInOutService {
    String CHECK_IN = "CHECK_IN";
    String CHECK_OUT = "CHECK_OUT";

    void checkIn(CheckInOutRequest checkInOutRequest);
    void checkOut(CheckInOutRequest checkInOutRequest);
}
