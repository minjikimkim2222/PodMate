package com.podmate.domain.cart.application;

import com.podmate.domain.cart.converter.CartItemConverter;
import com.podmate.domain.cart.domain.entity.CartItem;
import com.podmate.domain.cart.domain.repository.CartItemRepository;
import com.podmate.domain.cart.dto.CartRequestDto.CartCreateRequest;
import com.podmate.domain.cart.dto.CartRequestDto.CartItemRequest;
import com.podmate.domain.cart.dto.CartResponseDto.CartItemList;
import com.podmate.domain.cart.dto.CartResponseDto.PlatformList;
import com.podmate.domain.cart.exception.CartItemNotFoundException;
import com.podmate.domain.crawling.CrawlingService;
import com.podmate.domain.platformInfo.converter.PlatformConverter;
import com.podmate.domain.platformInfo.domain.entity.PlatformInfo;
import com.podmate.domain.platformInfo.domain.repository.PlatformInfoRepository;
import com.podmate.domain.platformInfo.exception.PlatformAccessDeniedException;
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
    private final CrawlingService crawlingService;
    private final CartItemRepository cartItemRepository;

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

    @Override
    public CartItemList getCartItems(Long userId, Long platformInfoId) {
        PlatformInfo platformInfo = platformInfoRepository.findById(platformInfoId)
                .orElseThrow(() -> new PlatformNotSupportedException());

        validateOwnerShip(findUser(userId), platformInfo);

        List<CartItem> cartItems = cartItemRepository.findAllByPlatformInfo(platformInfo);

        return CartItemConverter.toCartItemListResponse(cartItems);
    }

    @Override
    public String addCartItems(Long userId, CartItemRequest request) {
        User user = findUser(userId);
        PlatformInfo platformInfo = platformInfoRepository.findById(request.getPlatformInfoId())
                .orElseThrow(() -> new PlatformNotSupportedException());

        validateOwnerShip(user, platformInfo);

        // itemList 순회하며 각각 저장
        for (CartItemRequest.Item item : request.getItemList()){
            // TODO : 크롤링 서비스 적용 전
            String itemName = crawlingService.fetchItemName(item.getItemUrl());
            int price = crawlingService.fetchPrice(item.getItemUrl());

            CartItem cartItem = CartItem.builder()
                    .platformInfo(platformInfo)
                    .itemName(itemName)
                    .quantity(item.getQuantity())
                    .itemUrl(item.getItemUrl())
                    .optionText(item.getOptionText())
                    .price(price)
                    .build();

            cartItemRepository.save(cartItem);
        }

        return "장바구니 속 상품이 성공적으로 저장되었습니다.";
    }

    @Override
    public String deleteCartItem(Long userId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException());

        validateOwnerShip(userId, cartItem);

        cartItemRepository.delete(cartItem);

        return "장바구니 속 상품이 성공적으로 삭제되었습니다.";
    }

    private User findUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());
    }

    private void validateOwnerShip(Long userId, CartItem cartItem){
        Long ownerId = cartItem.getPlatformInfo().getUser().getId();

        if (!ownerId.equals(userId)){
            throw new PlatformAccessDeniedException();
        }
    }

    private void validateOwnerShip(User user, PlatformInfo platformInfo){
        // 해당 platformInfo가 본인의 장바구니가 맞는지 체크
        if (!platformInfo.getUser().getId().equals(user.getId())){
            throw new PlatformAccessDeniedException();
        }
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
