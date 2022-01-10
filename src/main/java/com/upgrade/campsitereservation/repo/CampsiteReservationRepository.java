package com.upgrade.campsitereservation.repo;

import com.upgrade.campsitereservation.entity.CampsiteReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CampsiteReservationRepository extends JpaRepository<CampsiteReservation, Long> {

    @Query(nativeQuery = true,
            value = "SELECT count(*) FROM CAMPSITE_RESERVATION " +
            "where (arrival_date >= ?1 AND arrival_date < ?2) " +
            "OR (departure_date > ?1 AND departure_date <= ?2)")
    long countReservationsBetween(LocalDate start, LocalDate end);
    List<CampsiteReservation> findAllByReservedByEmail(String email);
}
