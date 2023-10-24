package com.example.parkingmanagement.mapper;


import com.example.parkingmanagement.api.create.UserCreateRequest;
import com.example.parkingmanagement.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User map(UserCreateRequest userCreateRequest);

}
