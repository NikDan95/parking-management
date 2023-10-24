package com.example.parkingmanagement.mapper;

import com.example.parkingmanagement.api.create.CommunityCreateRequest;
import com.example.parkingmanagement.api.response.CommunityResponse;
import com.example.parkingmanagement.api.update.CommunityUpdateRequest;
import com.example.parkingmanagement.entity.Community;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface CommunityMapper {

    Community map(CommunityCreateRequest communityCreateRequest);

    CommunityResponse map(Community community);

    void map(CommunityUpdateRequest communityUpdateRequest, @MappingTarget Community community);

    @AfterMapping
    default void setCommunity(@MappingTarget Community community) {
        community.getSlots().forEach(slot -> slot.setCommunity(community));
        community.getUsers().forEach(user -> user.setCommunity(community));
    }
}
