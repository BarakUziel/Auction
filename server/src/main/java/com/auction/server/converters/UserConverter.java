package com.auction.server.converters;

import com.auction.server.dtos.UserDto;
import com.auction.server.entities.User;

public class UserConverter {

    public static User toEntity(UserDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }

    public static UserDto toDto(User entity) {
        UserDto dto = new UserDto();
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        return dto;
    }
}
