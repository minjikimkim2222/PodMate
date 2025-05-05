package com.podmate.domain.cart.application;

import com.podmate.domain.cart.dto.CartRequestDto.CartCreateRequest;
import com.podmate.domain.cart.dto.CartResponseDto.PlatformList;
import com.podmate.domain.platformInfo.converter.PlatformConverter;
import com.podmate.domain.platformInfo.domain.entity.PlatformInfo;
import com.podmate.domain.platformInfo.domain.repository.PlatformInfoRepository;
import com.podmate.domain.platformInfo.exception.PlatformNotSupportedException;
import com.podmate.domain.pod.domain.enums.Platform;
import com.podmate.domain.user.domain.entity.User;
import com.podmate.domain.user.domain.repository.UserRepository;
import com.podmate.domain.user.exception.UserNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final PlatformInfoRepository platformInfoRepository;

    @Override
    public String createCart(Long userId, CartCreateRequest request) {
        User user = findUser(userId);

        Platform platform = validatePlatform(request.getPlatformName());

        getOrCreatePlatformInfo(user, platform);

        return "장바구니가 생성되었습니다.";

    }

    @Override
    public PlatformList getCartList(Long userId) {
        User user = findUser(userId);

        List<PlatformInfo> platformInfos = platformInfoRepository.findAllByUser(user);

        return PlatformConverter.toPlatformList(platformInfos);
    }

    private User findUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());
    }

    // 유효한 플랫폼인지 확인 -- Pod의 Platform enum에 존재하는지 유효성 체크 함수
    private Platform validatePlatform(String platformName){
        return Platform.validatePlatform(platformName);
    }

    // 플랫폼 정보 조회 or 생성
    private PlatformInfo getOrCreatePlatformInfo(User user, Platform platform){
        log.info("==getOrCreatePlatformInfo== {}", platform.name());
        return platformInfoRepository.findByUserAndPlatformName(user, platform.name())
                .orElseGet(() -> platformInfoRepository.save(
                        PlatformInfo.builder()
                                .user(user)
                                .platformName(platform.name())
                                .build()
                ));
    }

}
