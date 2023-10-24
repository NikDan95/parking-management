package com.example.parkingmanagement.api.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import static com.example.parkingmanagement.constant.ParkingManagementConstant.PHONE_NUMBER_PATTERN;

@Getter
@Setter
public class UserCreateRequest {

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @Email
    private String email;

    @Pattern(regexp = PHONE_NUMBER_PATTERN)
    private String phone;
    private String username;

    @Size(min = 6)
    private String password;
}
