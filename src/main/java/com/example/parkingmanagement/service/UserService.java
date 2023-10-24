package com.example.parkingmanagement.service;

import com.example.parkingmanagement.api.create.UserCreateRequest;
import com.example.parkingmanagement.constant.Authority;
import com.example.parkingmanagement.entity.Community;
import com.example.parkingmanagement.entity.Role;
import com.example.parkingmanagement.entity.User;
import com.example.parkingmanagement.exception.IncorrectUsernameOrPasswordException;
import com.example.parkingmanagement.exception.GenericException;
import com.example.parkingmanagement.mapper.UserMapper;
import com.example.parkingmanagement.repository.CommunityRepository;
import com.example.parkingmanagement.repository.UserRepository;

import com.example.parkingmanagement.security.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.parkingmanagement.constant.MessagesConstant.COMMUNITY_NOT_FOUND;
import static com.example.parkingmanagement.constant.MessagesConstant.USER_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(IncorrectUsernameOrPasswordException::new);
        return new UserPrincipal(user);
    }

    public Long register(Long parkingId, UserCreateRequest userCreateRequest, Authority authority) {
        String username = userCreateRequest.getUsername();
        userRepository.findByUsername(username).ifPresent(user -> {
            throw new GenericException(USER_ALREADY_EXISTS);
        });
        String encodedPassword = getEncodedPassword(userCreateRequest);
        userCreateRequest.setPassword(encodedPassword);
        User newUser = userMapper.map(userCreateRequest);
        Community community = communityRepository.findById(parkingId)
                .orElseThrow(() -> new EntityNotFoundException(COMMUNITY_NOT_FOUND));
        newUser.setCommunity(community);
        newUser.setRoles(List.of(Role.builder()
                .user(newUser).authority(authority)
                .build()));
        User savedUser = userRepository.save(newUser);
        return savedUser.getId();
    }

    private String getEncodedPassword(UserCreateRequest userCreateRequest) {
        return new BCryptPasswordEncoder().encode(userCreateRequest.getPassword());
    }

    //    TODO add user update delete logic

}
