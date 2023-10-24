package com.example.parkingmanagement.controller;

import com.example.parkingmanagement.api.create.CommunityCreateRequest;
import com.example.parkingmanagement.api.response.CommunityResponse;
import com.example.parkingmanagement.api.update.CommunityUpdateRequest;
import com.example.parkingmanagement.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.parkingmanagement.constant.MessagesConstant.COMMUNITY_SUCCESSFULLY_DELETED;
import static com.example.parkingmanagement.constant.MessagesConstant.COMMUNITY_SUCCESSFULLY_UPDATED;

@Slf4j
@RestController
@RequestMapping("/communities")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping("/")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Long> save(@RequestBody CommunityCreateRequest communityCreateRequest) {
        log.info("started to create new parking");
        Long id = communityService.save(communityCreateRequest);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/{parkingId:\\d*}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> update(@PathVariable Long parkingId, @RequestBody CommunityUpdateRequest communityUpdateRequest) {
        log.info("trying to update parking");
        communityService.update(parkingId, communityUpdateRequest);
        return ResponseEntity.ok(COMMUNITY_SUCCESSFULLY_UPDATED);
    }

    @GetMapping("{parkingId:\\d*}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'RESIDENT')")
    public ResponseEntity<CommunityResponse> get(@PathVariable Long parkingId) {
        CommunityResponse response = communityService.get(parkingId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{parkingId:\\d*}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long parkingId) {
        communityService.delete(parkingId);
        return ResponseEntity.ok(COMMUNITY_SUCCESSFULLY_DELETED);
    }


}
