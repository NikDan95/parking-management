package com.example.parkingmanagement.entity;

import com.example.parkingmanagement.constant.Authority;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

import static com.example.parkingmanagement.constant.ParkingManagementConstant.ROLE_SEQUENCE;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ROLE_SEQUENCE)
    @SequenceGenerator(name = ROLE_SEQUENCE, sequenceName = ROLE_SEQUENCE, allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @ManyToOne
    private User user;

}
