package com.example.parkingmanagement.service;

import com.example.parkingmanagement.api.create.CommunityCreateRequest;
import com.example.parkingmanagement.api.response.CommunityResponse;
import com.example.parkingmanagement.api.update.CommunityUpdateRequest;
import com.example.parkingmanagement.entity.Community;
import com.example.parkingmanagement.mapper.CommunityMapper;
import com.example.parkingmanagement.repository.CommunityRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.parkingmanagement.constant.MessagesConstant.COMMUNITY_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityMapper communityMapper;

    public Long save(CommunityCreateRequest communityCreateRequest) {
        Community community = communityRepository.save(communityMapper.map(communityCreateRequest));
        return community.getId();
    }

    public void update(Long id, CommunityUpdateRequest communityUpdateRequest) {
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(COMMUNITY_NOT_FOUND));
        communityMapper.map(communityUpdateRequest, community);
    }

    public CommunityResponse get(Long id) {
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(COMMUNITY_NOT_FOUND));
        return communityMapper.map(community);
    }

    public void delete(Long communityId) {
        communityRepository.deleteById(communityId);
    }

}
