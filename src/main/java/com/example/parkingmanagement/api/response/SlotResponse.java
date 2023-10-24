package com.example.parkingmanagement.api.response;

import com.example.parkingmanagement.entity.Booking;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SlotResponse {

    private Long id;
    private Integer slotNumber;
    private String parkedUsername;
    private List<Booking> bookings;
}
