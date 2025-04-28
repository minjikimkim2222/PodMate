package com.podmate.domain.user.application;

import com.podmate.domain.address.domain.entity.Address;
import com.podmate.domain.address.domain.repository.AddressRepository;
import com.podmate.domain.address.dto.AddressRequestDto.UserAddressUpdateRequest;
import com.podmate.domain.user.converter.UserConverter;
import com.podmate.domain.user.domain.entity.User;
import com.podmate.domain.user.domain.repository.UserRepository;
import com.podmate.domain.user.dto.UserResponseDto;
import com.podmate.domain.user.dto.UserResponseDto.AddressInfo;
import com.podmate.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    @Override
    public UserResponseDto.MyInfo getMyInfo(User user) {
        return UserConverter.toMyInfo(user);
    }

    @Override
    public AddressInfo updateAddress(Long userId, UserAddressUpdateRequest addressUpdateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        Address address = user.updateAddress(addressUpdateRequest, addressRepository);

        return new UserResponseDto.AddressInfo(user.getId(), address.getId());
    }

}
