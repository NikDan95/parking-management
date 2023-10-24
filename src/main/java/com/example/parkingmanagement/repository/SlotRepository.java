package com.example.parkingmanagement.repository;

import com.example.parkingmanagement.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {

    @Query("SELECT s FROM Slot s WHERE s.community.id=:communityId AND s.id IN (" +
            "SELECT DISTINCT b.slot.id FROM Booking b " +
            "WHERE b.startDate <= :endDate OR b.endDate >= :startDate)")
    List<Slot> findAvailableSlotsByParkingId(Long communityId, LocalDateTime startDate, LocalDateTime endDate);

}
