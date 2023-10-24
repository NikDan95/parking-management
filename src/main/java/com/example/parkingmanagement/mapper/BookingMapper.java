package com.example.parkingmanagement.mapper;

import com.example.parkingmanagement.api.create.BookingCreateRequest;
import com.example.parkingmanagement.entity.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    Booking map(BookingCreateRequest bookingCreateRequest);
}
