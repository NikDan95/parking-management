package com.example.parkingmanagement.constant;

public class MessagesConstant {

    private MessagesConstant(){
    }

    public static final String COMMUNITY_NOT_FOUND = "community not found";
    public static final String USER_ALREADY_EXISTS = "username already exists";
    public static final String USER_NOT_ALLOWED = "user  don't have permission for this community";
    public static final String BOOKING_NOT_FOUND = "there is no booking for current user";
    public static final String BOOKING_EXPIRED = "the booking has expired";
    public static final String INVALID_SLOT_ID = "slot id is invalid";
    public static final String INVALID_BOOKING_PERIOD = "startDate or endDate are incorrect";
    public static final String OVERLAP_BOOKING_PERIOD = "provided period is overlapping with existing one";
    public static final String COMMUNITY_SUCCESSFULLY_UPDATED = "community successfully updated";
    public static final String COMMUNITY_SUCCESSFULLY_DELETED = "community successfully deleted";
    public static final String SUCCESSFULLY_BOOKING = "booking successfully done";
    public static final String SUCCESSFULLY_RELEASED = "booking successfully released";
}
