package com.podmate.domain.user.application;

import com.podmate.domain.user.domain.entity.User;
import com.podmate.domain.user.dto.UserResponseDto;

public interface UserService {
    UserResponseDto.MyInfo getMyInfo(User user);
}
