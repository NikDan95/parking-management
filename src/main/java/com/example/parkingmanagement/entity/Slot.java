package com.example.parkingmanagement.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.example.parkingmanagement.constant.ParkingManagementConstant.SLOT_SEQUENCE;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SLOT_SEQUENCE)
    @SequenceGenerator(name = SLOT_SEQUENCE, sequenceName = SLOT_SEQUENCE, allocationSize = 1)
    private Long id;

    @OneToMany(mappedBy = "slot", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

    @Version
    private Long version;

    @ManyToOne
    private Community community;
}
