package com.example.parkingmanagement.controller;

import com.example.parkingmanagement.api.create.UserCreateRequest;
import com.example.parkingmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.parkingmanagement.constant.Authority.COMMUNITY_ADMIN;
import static com.example.parkingmanagement.constant.Authority.RESIDENT;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/superAdmin")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
    public ResponseEntity<Long> registerSuperAdmin(@RequestBody UserCreateRequest userCreateRequest) {
        return ResponseEntity.ok(userService.register(null, userCreateRequest, COMMUNITY_ADMIN));
    }

    @PostMapping("/{parkingId:\\d*}/admin")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Long> registerCommunityAdmin(@PathVariable Long parkingId,
                                              @RequestBody UserCreateRequest userCreateRequest) {
        return ResponseEntity.ok(userService.register(parkingId, userCreateRequest, COMMUNITY_ADMIN));
    }

    @PostMapping("/{parkingId:\\d*}/resident")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Long> registerResident(@PathVariable Long parkingId,
                                                 @RequestBody UserCreateRequest userCreateRequest) {
        return ResponseEntity.ok(userService.register(parkingId, userCreateRequest, RESIDENT));
    }

    //TODO add deletion and update endpoints
}
