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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static com.example.constant.TestConstants.MOCK_COMMUNITY_ID;
import static com.example.constant.TestConstants.MOCK_SLOT_ID;
import static com.example.parkingmanagement.constant.MessagesConstant.BOOKING_NOT_FOUND;
import static com.example.parkingmanagement.constant.MessagesConstant.COMMUNITY_NOT_FOUND;
import static com.example.parkingmanagement.util.TestUtil.addBidirectionalMapping;
import static com.example.parkingmanagement.util.TestUtil.buildBookingModel;
import static com.example.parkingmanagement.util.TestUtil.buildBookingRequest;
import static com.example.parkingmanagement.util.TestUtil.buildParkingModel;
import static com.example.parkingmanagement.util.TestUtil.buildSlotModel;
import static com.example.parkingmanagement.util.TestUtil.buildUserModel;
import static com.example.parkingmanagement.util.TestUtil.buildUserPrincipal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommunityManagementServiceTest {

    @InjectMocks
    CommunityManagementService communityManagementService;

    @Mock
    CommunityRepository communityRepository;

    @Mock
    SlotRepository slotRepository;

    @BeforeEach
    void setup() {
        UsernamePasswordAuthenticationToken mockUsernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(buildUserPrincipal(), null);
        SecurityContextHolder.getContext()
                .setAuthentication(mockUsernamePasswordAuthenticationToken);

        ReflectionTestUtils.setField(communityManagementService, "minPeriod", 30L);
    }

    //TODO cover all cases

    @Test
    void successfully_book_slot() {
        BookingCreateRequest bookingCreateRequest = buildBookingRequest();
        Community community = buildParkingModel();
        User user = buildUserModel();
        user.setCommunity(community);
        community.getUsers().add(user);
        Slot slot = buildSlotModel();
        slot.setId(MOCK_SLOT_ID);
        community.getSlots().add(slot);
        when(communityRepository.findById(MOCK_COMMUNITY_ID)).thenReturn(Optional.of(community));
        communityManagementService.bookSlot(MOCK_COMMUNITY_ID, bookingCreateRequest);
        verify(communityRepository).findById(MOCK_COMMUNITY_ID);
    }

    @Test
    void book_slot_in_non_existing_parking() {
        BookingCreateRequest bookingCreateRequest = buildBookingRequest();

        assertThrows(EntityNotFoundException.class,
                () -> communityManagementService.bookSlot(MOCK_COMMUNITY_ID, bookingCreateRequest),
                COMMUNITY_NOT_FOUND);
        verify(communityRepository).findById(MOCK_COMMUNITY_ID);
    }

    @Test
    void successfully_release_slot() {
        Community community = buildParkingModel();
        User user = buildUserModel();
        Slot slot = buildSlotModel();
        slot.setId(MOCK_SLOT_ID);
        Booking booking = buildBookingModel();
        addBidirectionalMapping(community, user, slot, booking);
        when(communityRepository.findById(MOCK_COMMUNITY_ID)).thenReturn(Optional.of(community));

        communityManagementService.releaseSlot(MOCK_COMMUNITY_ID, MOCK_SLOT_ID);

        verify(communityRepository).findById(MOCK_COMMUNITY_ID);
    }

    @Test
    void release_not_existing_booking() {
        Community community = buildParkingModel();
        User user = buildUserModel();
        Slot slot = buildSlotModel();
        community.getUsers().add(user);
        user.setCommunity(community);
        community.getSlots().add(slot);
        slot.setCommunity(community);
        when(communityRepository.findById(MOCK_COMMUNITY_ID)).thenReturn(Optional.of(community));

        assertThrows(GenericException.class,
                () -> communityManagementService.releaseSlot(MOCK_COMMUNITY_ID, MOCK_SLOT_ID),
                BOOKING_NOT_FOUND);
        verify(communityRepository).findById(MOCK_COMMUNITY_ID);
    }

    @Test
    void get_available_slots() {
        List<Slot> slots = List.of(buildSlotModel());
        when(slotRepository.findAvailableSlotsByParkingId(any(), any(), any())).thenReturn(slots);
        List<Slot> availableSlots = communityManagementService.getAvailableSlots(MOCK_COMMUNITY_ID);
        assertEquals(slots, availableSlots);
        verify(slotRepository).findAvailableSlotsByParkingId(any(), any(), any());
    }


}