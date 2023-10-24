package com.example.parkingmanagement.controller;


import com.example.parkingmanagement.api.create.BookingCreateRequest;
import com.example.parkingmanagement.entity.Booking;
import com.example.parkingmanagement.entity.Community;
import com.example.parkingmanagement.entity.Slot;
import com.example.parkingmanagement.entity.User;
import com.example.parkingmanagement.repository.CommunityRepository;
import com.example.parkingmanagement.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.constant.TestConstants.BOOK_SLOT_API;
import static com.example.constant.TestConstants.MOCK_USER_USERNAME;
import static com.example.constant.TestConstants.RELEASE_SLOT_API;
import static com.example.parkingmanagement.constant.MessagesConstant.INVALID_SLOT_ID;
import static com.example.parkingmanagement.constant.MessagesConstant.SUCCESSFULLY_BOOKING;
import static com.example.parkingmanagement.constant.MessagesConstant.SUCCESSFULLY_RELEASED;
import static com.example.parkingmanagement.util.TestUtil.addBidirectionalMapping;
import static com.example.parkingmanagement.util.TestUtil.buildBookingModel;
import static com.example.parkingmanagement.util.TestUtil.buildBookingRequest;
import static com.example.parkingmanagement.util.TestUtil.buildParkingModel;
import static com.example.parkingmanagement.util.TestUtil.buildGrantedAuthorities;
import static com.example.parkingmanagement.util.TestUtil.buildSlotModel;
import static com.example.parkingmanagement.util.TestUtil.buildUserModel;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@ActiveProfiles("test")
@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommunityManagementControllerTest {

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${com.example.jwtSecret}")
    private String jwtSecret;

    @Test
    @Order(1)
    void successfully_book_slot() {
        Community community = buildParkingModel();
        User user = buildUserModel();
        user.setCommunity(community);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.getRoles().forEach(role -> role.setUser(user));
        Slot slot = buildSlotModel();
        addBidirectionalMapping(community, user, slot);
        Community savedCommunity = communityRepository.save(community);
        userRepository.save(user);
        BookingCreateRequest bookingCreateRequest = buildBookingRequest();
        ResponseEntity<String> responseEntity = put(BOOK_SLOT_API + savedCommunity.getId(), bookingCreateRequest, String.class);
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(SUCCESSFULLY_BOOKING, responseEntity.getBody());
        communityRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Order(2)
    void book_non_existing_slot() {
        Community community = buildParkingModel();
        User user = buildUserModel();
        user.setCommunity(community);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.getRoles().forEach(role -> role.setUser(user));
        community.getUsers().add(user);
        user.setCommunity(community);
        Community savedCommunity = communityRepository.save(community);
        userRepository.save(user);
        BookingCreateRequest bookingCreateRequest = buildBookingRequest();
        ResponseEntity<String> responseEntity = put(BOOK_SLOT_API + savedCommunity.getId(), bookingCreateRequest, String.class);
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(INVALID_SLOT_ID, responseEntity.getBody());
        communityRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Order(3)
    void successfully_release_booking() {
        Community community = buildParkingModel();
        User user = buildUserModel();
        user.setCommunity(community);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.getRoles().forEach(role -> role.setUser(user));
        Slot slot = buildSlotModel();
        Booking booking = buildBookingModel();
        booking.setUser(user);
        booking.setSlot(slot);
        slot.getBookings().add(booking);
        user.getBookings().add(booking);
        addBidirectionalMapping(community, user, slot);
        Community savedCommunity = communityRepository.save(community);
        userRepository.save(user);
        BookingCreateRequest bookingCreateRequest = buildBookingRequest();
        ResponseEntity<String> responseEntity = put(RELEASE_SLOT_API + savedCommunity.getId() + "?slotId=2", null, String.class);
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(SUCCESSFULLY_RELEASED, responseEntity.getBody());
        communityRepository.deleteAll();
        userRepository.deleteAll();
    }

    private <T, J> ResponseEntity<T> put(final String url, J requestBody, Class<T> returnType) {
        return restTemplate.exchange(url,
                HttpMethod.PUT,
                new HttpEntity<>(requestBody, addSecurityHeaders(new HttpHeaders())),
                returnType);
    }

    private ResponseEntity<String> putTest(final String url, long slotId) {
        Map<String, Long> requestBody = Map.of("slotId", slotId);
        return restTemplate.exchange(url,
                HttpMethod.PUT,
                new HttpEntity<>(requestBody, addSecurityHeaders(new HttpHeaders())),
                String.class);
    }

    private HttpHeaders addSecurityHeaders(HttpHeaders headers) {
        headers.add(AUTHORIZATION, generateJwtToken());
        return headers;
    }

    private String generateJwtToken() {
        Date now = new Date();
        Date expiredOn = new Date(now.getTime() + 1000000);
        String compact = Jwts.builder()
                .claim("username", MOCK_USER_USERNAME)
                .claim("roles", buildGrantedAuthorities())
                .setIssuedAt(now)
                .setExpiration(expiredOn)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
        return "Bearer " + compact;
    }

}