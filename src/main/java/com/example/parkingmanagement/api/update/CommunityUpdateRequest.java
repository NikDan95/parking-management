package com.example.parkingmanagement.api.update;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommunityUpdateRequest {

    private String name;
    private List<SlotUpdateRequest> slots;
}
