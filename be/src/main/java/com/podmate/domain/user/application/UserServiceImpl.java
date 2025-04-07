package com.podmate.domain.user.application;

import com.podmate.domain.user.converter.UserConverter;
import com.podmate.domain.user.domain.entity.User;
import com.podmate.domain.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    @Override
    public UserResponseDto.MyInfo getMyInfo(User user) {
        return UserConverter.toMyInfo(user);
    }

}
