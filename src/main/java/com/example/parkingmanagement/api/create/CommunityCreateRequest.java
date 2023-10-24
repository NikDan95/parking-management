package com.example.parkingmanagement.api.create;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommunityCreateRequest {

    private String name;
    private String location;
    private List<SlotCreateRequest> slots;
}
