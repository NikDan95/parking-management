package com.example.parkingmanagement.repository;

import com.example.parkingmanagement.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long> {
}
