package com.example.parkingmanagement.service;

import com.example.parkingmanagement.api.create.BookingCreateRequest;
import com.example.parkingmanagement.entity.Booking;
import com.example.parkingmanagement.entity.Community;
import com.example.parkingmanagement.entity.Slot;
import com.example.parkingmanagement.entity.User;
import com.example.parkingmanagement.exception.GenericException;
import com.example.parkingmanagement.repository.CommunityRepository;
import com.example.parkingmanagement.repository.SlotRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.example.parkingmanagement.constant.MessagesConstant.BOOKING_EXPIRED;
import static com.example.parkingmanagement.constant.MessagesConstant.BOOKING_NOT_FOUND;
import static com.example.parkingmanagement.constant.MessagesConstant.INVALID_BOOKING_PERIOD;
import static com.example.parkingmanagement.constant.MessagesConstant.INVALID_SLOT_ID;
import static com.example.parkingmanagement.constant.MessagesConstant.OVERLAP_BOOKING_PERIOD;
import static com.example.parkingmanagement.constant.MessagesConstant.COMMUNITY_NOT_FOUND;
import static com.example.parkingmanagement.constant.MessagesConstant.USER_NOT_ALLOWED;

@Service
@RequiredArgsConstructor
public class CommunityManagementService {

    private final CommunityRepository communityRepository;
    private final SlotRepository slotRepository;

    @Value("${com.example.parkingmanagement.minBookingPeriod}")
    private Long minPeriod;

    @Transactional
    public void bookSlot(Long parkingId, BookingCreateRequest bookingCreateRequest) {
        Community community = getParkingById(parkingId);
        User user = validateAndGetUser(getUsernameFromSecurityContext(), community);
        validateBookingPeriod(bookingCreateRequest);
        Slot slot = community.getSlots().stream()
                .filter(slotEntity -> Objects.equals(bookingCreateRequest.getSlotId(), slotEntity.getId()))
                .findFirst().orElseThrow(() -> new GenericException(INVALID_SLOT_ID));
        slot.getBookings().forEach(booking ->
                validateNotOverlapping(bookingCreateRequest.getStartTime(), booking.getEndDate(), booking));
        Booking newBooking = buildBooking(bookingCreateRequest, user, slot);
        slot.getBookings().add(newBooking);
        user.getBookings().add(newBooking);
    }

    @Transactional
    public void releaseSlot(Long parkingId, Long slotId) {
        String username = getUsernameFromSecurityContext();
        Community community = getParkingById(parkingId);
        Slot slot = getSlotFromParking(slotId, community);
        User user = validateAndGetUser(username, community);
        Booking booking = slot.getBookings().stream()
                .filter(bookingEntity -> bookingEntity.getUser().equals(user))
                .findFirst().orElseThrow(() -> new GenericException(BOOKING_NOT_FOUND));
        LocalDateTime now = LocalDateTime.now();
        if (booking.getEndDate().isBefore(now)){
            throw new GenericException(BOOKING_EXPIRED);
        }
        booking.setEndDate(now);
    }

    public List<Slot> getAvailableSlots(Long parkingId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minEndDate = now.plusMinutes(minPeriod);
        return slotRepository.findAvailableSlotsByParkingId(parkingId, now, minEndDate);
    }

    private Community getParkingById(Long parkingId) {
        return communityRepository.findById(parkingId)
                .orElseThrow(() -> new EntityNotFoundException(COMMUNITY_NOT_FOUND));
    }


    private Slot getSlotFromParking(Long slotId, Community community) {
        return community.getSlots().stream()
                .filter(slotEntity -> Objects.equals(slotId, slotEntity.getId()))
                .findFirst()
                .orElseThrow(() -> new GenericException(INVALID_SLOT_ID));
    }

    private User validateAndGetUser(String username, Community community) {
        return community.getUsers().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst().orElseThrow(() -> new GenericException(USER_NOT_ALLOWED));
    }

    private void validateBookingPeriod(BookingCreateRequest bookingCreateRequest) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = bookingCreateRequest.getStartTime();
        LocalDateTime endTime = bookingCreateRequest.getEndTime();
        if (startTime.isBefore(now) ||
                startTime.isAfter(endTime) ||
                startTime.plusMinutes(minPeriod).isAfter(endTime)) {
            throw new GenericException(INVALID_BOOKING_PERIOD);
        }
    }

    private void validateNotOverlapping(LocalDateTime startDate, LocalDateTime endDate, Booking booking) {
        boolean isNotOverlapping = startDate.isAfter(booking.getEndDate()) ||
                endDate.isBefore(booking.getStartDate());
        if (!isNotOverlapping) {
            throw new GenericException(OVERLAP_BOOKING_PERIOD);
        }
    }

    private Booking buildBooking(BookingCreateRequest bookingCreateRequest, User user, Slot slot) {
        return Booking.builder()
                .user(user)
                .slot(slot)
                .startDate(bookingCreateRequest.getStartTime())
                .endDate(bookingCreateRequest.getEndTime())
                .build();
    }

    private String getUsernameFromSecurityContext() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
