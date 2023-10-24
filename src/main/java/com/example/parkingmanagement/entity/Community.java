package com.example.parkingmanagement.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.example.parkingmanagement.constant.ParkingManagementConstant.COMMUNITY_SEQUENCE;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = COMMUNITY_SEQUENCE)
    @SequenceGenerator(name = COMMUNITY_SEQUENCE, sequenceName = COMMUNITY_SEQUENCE, allocationSize = 1)
    private Long id;
    private String name;
    private String location;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
    private List<Slot> slots = new ArrayList<>();

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

}
