package com.auction.server.dtos;

import lombok.Data;

@Data
public class UserDto {
    private String name;
    private String email;
    private String password;
}