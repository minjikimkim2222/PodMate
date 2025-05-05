package com.podmate.domain.user.converter;

import com.podmate.domain.user.domain.entity.User;
import com.podmate.domain.user.dto.UserResponseDto;

public class UserConverter {
    public static UserResponseDto.MyInfo toMyInfo(User user){
        return UserResponseDto.MyInfo.builder()
                .userId(user.getId())
                .role(user.getRole().getName())
                .build();
    }
}
