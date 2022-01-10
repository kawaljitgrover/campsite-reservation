package com.upgrade.campsitereservation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "campsite_reservation")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CampsiteReservation {
    @Id
    @GeneratedValue
    @Column(name = "reservation_id")
    private long id;
    @Column(name = "reserved_by_email_address")
    private String reservedByEmail;
    @Column(name = "number_guests")
    private int guests = 1;
    @Column(name = "reservation_date")
    @JsonFormat(pattern="MM/dd/yyyy HH:mm")
    private LocalDateTime reservedOn;
    @Column(name = "arrival_date")
    @JsonFormat(pattern="MM/dd/yyyy")
    private LocalDate arrival;
    @Column(name = "departure_date")
    @JsonFormat(pattern="MM/dd/yyyy")
    private LocalDate departure;
    @Column(name = "check_in_date")
    @JsonFormat(pattern="MM/dd/yyyy HH:mm")
    private LocalDateTime checkIn;
    @Column(name = "check_out_date")
    @JsonFormat(pattern="MM/dd/yyyy HH:mm")
    private LocalDateTime checkOut;
}