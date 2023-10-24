package com.example.parkingmanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.example.parkingmanagement.constant.ParkingManagementConstant.BOOKING_SEQUENCE;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = BOOKING_SEQUENCE)
    @SequenceGenerator(name = BOOKING_SEQUENCE, sequenceName = BOOKING_SEQUENCE, allocationSize = 1)
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ManyToOne
    private Slot slot;

    @ManyToOne
    private User user;

}
