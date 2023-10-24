package com.example.parkingmanagement.util;


import com.example.parkingmanagement.api.create.BookingCreateRequest;
import com.example.parkingmanagement.constant.Authority;
import com.example.parkingmanagement.entity.Booking;
import com.example.parkingmanagement.entity.Community;
import com.example.parkingmanagement.entity.Role;
import com.example.parkingmanagement.entity.Slot;
import com.example.parkingmanagement.entity.User;
import com.example.parkingmanagement.security.UserPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.constant.TestConstants.MOCK_BOOKING_ID;
import static com.example.constant.TestConstants.MOCK_COMMUNITY_ID;
import static com.example.constant.TestConstants.MOCK_COMMUNITY_LOCATION;
import static com.example.constant.TestConstants.MOCK_COMMUNITY_NAME;
import static com.example.constant.TestConstants.MOCK_ROLE_ID;
import static com.example.constant.TestConstants.MOCK_SLOT_ID;
import static com.example.constant.TestConstants.MOCK_USER_EMAIL;
import static com.example.constant.TestConstants.MOCK_USER_FIRSTNAME;
import static com.example.constant.TestConstants.MOCK_USER_ID;
import static com.example.constant.TestConstants.MOCK_USER_LASTNAME;
import static com.example.constant.TestConstants.MOCK_USER_PASSWORD;
import static com.example.constant.TestConstants.MOCK_USER_PHONE;
import static com.example.constant.TestConstants.MOCK_USER_USERNAME;
import static com.example.parkingmanagement.constant.Authority.COMMUNITY_ADMIN;
import static com.example.parkingmanagement.constant.Authority.RESIDENT;
import static com.example.parkingmanagement.constant.Authority.SUPER_ADMIN;

public class TestUtil {

    private TestUtil() {

    }

    public static BookingCreateRequest buildBookingRequest() {
        return BookingCreateRequest.builder()
                .slotId(MOCK_SLOT_ID)
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusHours(2))
                .build();
    }

    public static Community buildParkingModel() {
        return Community.builder()
                .name(MOCK_COMMUNITY_NAME)
                .location(MOCK_COMMUNITY_LOCATION)
                .slots(new ArrayList<>())
                .users(new ArrayList<>())
                .build();
    }

    public static Booking buildBookingModel() {
        return Booking.builder()
                .startDate(LocalDateTime.now().plusSeconds(1))
                .endDate(LocalDateTime.now().plusHours(3))
                .build();
    }

    public static User buildUserModel() {
        return User.builder()
                .firstname(MOCK_USER_FIRSTNAME)
                .lastname(MOCK_USER_LASTNAME)
                .email(MOCK_USER_EMAIL)
                .phone(MOCK_USER_PHONE)
                .username(MOCK_USER_USERNAME)
                .password(MOCK_USER_PASSWORD)
                .bookings(new ArrayList<>())
                .roles(List.of(buildRoleModel(RESIDENT)))
                .build();
    }

    public static Collection<? extends GrantedAuthority> buildGrantedAuthorities() {
        List<Role> roles = List.of(buildRoleModel(RESIDENT),
                buildRoleModel(COMMUNITY_ADMIN),
                buildRoleModel(SUPER_ADMIN));
        return roles.stream()
                .map(role -> role.getAuthority().name())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public static Role buildRoleModel(Authority authority) {
        return Role.builder()
                .authority(authority)
                .build();
    }

    public static Slot buildSlotModel() {
        return Slot.builder()
                .bookings(new ArrayList<>())
                .version(0L)
                .build();
    }


    public static void addBidirectionalMapping(Community community, User user, Slot slot, Booking booking) {
        addBidirectionalMapping(community, user, slot);
        user.getBookings().add(booking);
        slot.getBookings().add(booking);
        booking.setSlot(slot);
        booking.setUser(user);
    }

    public static void addBidirectionalMapping(Community community, User user, Slot slot) {
        community.getUsers().add(user);
        user.setCommunity(community);
        community.getSlots().add(slot);
        slot.setCommunity(community);
    }

    public static UserPrincipal buildUserPrincipal() {
        return new UserPrincipal(buildUserModel());
    }
}
