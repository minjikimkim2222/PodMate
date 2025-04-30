package com.podmate.domain.user.application;

import com.podmate.domain.address.domain.entity.Address;
import com.podmate.domain.address.domain.repository.AddressRepository;
import com.podmate.domain.address.dto.AddressRequestDto.UserAddressUpdateRequest;
import com.podmate.domain.review.domain.entity.Review;
import com.podmate.domain.review.domain.entity.ReviewOptionMapping;
import com.podmate.domain.review.domain.repository.ReviewOptionMappingRepository;
import com.podmate.domain.review.domain.repository.ReviewRepository;
import com.podmate.domain.token.domain.entity.Token;
import com.podmate.domain.token.domain.repository.TokenRepository;
import com.podmate.domain.token.exception.RefreshTokenNotFoundException;
import com.podmate.domain.user.converter.UserConverter;
import com.podmate.domain.user.domain.entity.User;
import com.podmate.domain.user.domain.repository.UserRepository;
import com.podmate.domain.user.dto.UserRequestDto.AccountRequestDto;
import com.podmate.domain.user.dto.UserResponseDto;
import com.podmate.domain.user.dto.UserResponseDto.AccountInfo;
import com.podmate.domain.user.dto.UserResponseDto.AddressInfo;
import com.podmate.domain.user.dto.UserResponseDto.OtherUserProfileInfo;
import com.podmate.domain.user.dto.UserResponseDto.OtherUserProfileInfo.Profile;
import com.podmate.domain.user.exception.UserNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final TokenRepository tokenRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewOptionMappingRepository reviewOptionMappingRepository;
    @Override
    public UserResponseDto.MyInfo getMyInfo(User user) {
        return UserConverter.toMyInfo(user);
    }

    @Override
    public UserResponseDto.AddressInfo updateAddress(Long userId, UserAddressUpdateRequest addressUpdateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        Address address = user.updateAddress(addressUpdateRequest, addressRepository);

        return new UserResponseDto.AddressInfo(user.getId(), address.getId());
    }

    @Override
    public UserResponseDto.AccountInfo updateAccount(Long userId, AccountRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        user.updateAccount(requestDto.getAccount());

        return new UserResponseDto.AccountInfo(user.getId(), user.getAccount());
    }

    @Override
    public void logout(Long userId) {
        Token token = tokenRepository.findByUserId(userId)
                .orElseThrow(() -> new RefreshTokenNotFoundException());
        tokenRepository.delete(token);
    }

    @Override
    public UserResponseDto.OtherUserProfileInfo getOtherUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        OtherUserProfileInfo.Profile profile = new OtherUserProfileInfo.Profile(
                user.getNickname(),
                user.getProfileImage(),
                user.getMannerScore()
        );

        List<Review> reviews = reviewRepository.findAllByRecipientId(userId);
        int reviewCount = reviews.size();

        List<OtherUserProfileInfo.Review> reviewDtos = reviews.stream()
                .map(review -> {
                    List<ReviewOptionMapping> mappings = reviewOptionMappingRepository.findAllByReviewId(
                            review.getId());

                    List<String> reviewOptions = mappings.stream()
                            .map(mapping -> mapping.getReviewOption().getOptionText().getReviewText())
                            .collect(Collectors.toList());

                    return new OtherUserProfileInfo.Review(review.getId(), reviewOptions, reviewCount);
                })
                .collect(Collectors.toList());

        return new OtherUserProfileInfo(profile, reviewDtos);
    }

}
