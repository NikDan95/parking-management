package com.example.parkingmanagement.controller;

import com.example.parkingmanagement.api.create.BookingCreateRequest;
import com.example.parkingmanagement.entity.Slot;
import com.example.parkingmanagement.service.CommunityManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.parkingmanagement.constant.MessagesConstant.SUCCESSFULLY_BOOKING;
import static com.example.parkingmanagement.constant.MessagesConstant.SUCCESSFULLY_RELEASED;

@Slf4j
@Validated
@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityManagementController {

    private final CommunityManagementService communityManagementService;

    @GetMapping(value = "{communityId:\\d*}")
    @PreAuthorize("hasAnyAuthority('ADMIN','RESIDENT')")
    public ResponseEntity<List<Slot>> getAvailableSlots(@PathVariable Long communityId) {
        List<Slot> availableSlots = communityManagementService.getAvailableSlots(communityId);
        return ResponseEntity.ok(availableSlots);
    }

    @PutMapping("/book/{communityId:\\d*}")
    @PreAuthorize("hasAuthority('RESIDENT')")
    public ResponseEntity<String> bookSlot(@PathVariable Long communityId, @RequestBody BookingCreateRequest bookingCreateRequest) {
        log.info("started booking process");
        communityManagementService.bookSlot(communityId, bookingCreateRequest);
        return ResponseEntity.ok(SUCCESSFULLY_BOOKING);
    }

    @PutMapping(value = "/release/{communityId:\\d*}")
    @PreAuthorize("hasAuthority('RESIDENT')")
    public ResponseEntity<String> release(@PathVariable Long communityId, @RequestParam("slotId") Long slotId) {
        log.info("started to release slot");
        communityManagementService.releaseSlot(communityId, slotId);
        return ResponseEntity.ok(SUCCESSFULLY_RELEASED);
    }

}
