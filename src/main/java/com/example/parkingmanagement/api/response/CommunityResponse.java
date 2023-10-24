package com.example.parkingmanagement.api.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommunityResponse {

    private Long id;
    private String name;
    private String location;
    private List<SlotResponse> slots;
}
