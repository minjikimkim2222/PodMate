package com.podmate.domain.user.application;

import com.podmate.domain.address.dto.AddressRequestDto;
import com.podmate.domain.user.domain.entity.User;
import com.podmate.domain.user.dto.UserRequestDto;
import com.podmate.domain.user.dto.UserResponseDto;

public interface UserService {
    UserResponseDto.MyInfo getMyInfo(User user);

    UserResponseDto.AddressInfo updateAddress(Long userId, AddressRequestDto.AddressUpdateRequest addressUpdateRequest);

    UserResponseDto.AccountInfo updateAccount(Long userId, UserRequestDto.AccountRequestDto requestDto);

    void logout(Long userId);

    UserResponseDto.OtherUserProfileInfo getOtherUserProfile(Long userId);
}
