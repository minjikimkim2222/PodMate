package com.podmate.domain.orderForm.application;

import com.podmate.domain.cart.domain.entity.CartItem;
import com.podmate.domain.cart.domain.repository.CartItemRepository;
import com.podmate.domain.orderForm.domain.entity.OrderForm;
import com.podmate.domain.orderForm.domain.entity.OrderItem;
import com.podmate.domain.orderForm.domain.repository.OrderFormRepository;
import com.podmate.domain.orderForm.domain.repository.OrderItemRepository;
import com.podmate.domain.platformInfo.exception.PlatformAccessDeniedException;
import com.podmate.domain.pod.domain.entity.Pod;
import com.podmate.domain.pod.domain.enums.Platform;
import com.podmate.domain.pod.domain.repository.PodRepository;
import com.podmate.domain.pod.exception.PodNotFoundException;
import com.podmate.domain.podUserMapping.domain.entity.PodUserMapping;
import com.podmate.domain.podUserMapping.domain.enums.IsApproved;
import com.podmate.domain.podUserMapping.domain.enums.PodRole;
import com.podmate.domain.podUserMapping.domain.repository.PodUserMappingRepository;
import com.podmate.domain.user.domain.entity.User;
import com.podmate.domain.user.domain.repository.UserRepository;
import com.podmate.domain.user.exception.UserNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderFormServiceImpl implements OrderFormService{
    private final UserRepository userRepository;
    private final PodRepository podRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderFormRepository orderFormRepository;
    private final OrderItemRepository orderItemRepository;
    private final PodUserMappingRepository podUserMappingRepository;
    @Override
    public Long createOrderForm(Long userId, Long podId, List<Long> itemIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        Pod pod = podRepository.findById(podId)
                .orElseThrow(() -> new PodNotFoundException());

        // itemIds로 장바구니 아이템 조회
        List<CartItem> cartItems = cartItemRepository.findAllById(itemIds);

        validateCartItemOwnership(userId, cartItems);
        int totalAmount = calculateTotalAmount(cartItems);
        Platform platform = extractPlatformFromCartItems(cartItems);

        // 1. OrderForm 생성 및 저장
        OrderForm orderForm = saveOrderForm(user, totalAmount, platform);

        // 2. 주문상품 (OrderItem) 생성 및 저장
        saveOrderItems(orderForm, cartItems);

        // 3. 팟-사용자 매핑 테이블 등록
        savePodUserMapping(pod, user, orderForm);

        return orderForm.getId(); // 생성한 주문서 Id 반환
    }

    // 유저 권한 체크 (내 장바구니 맞는지)
    private void validateCartItemOwnership(Long userId, List<CartItem> cartItems) {
        for (CartItem item : cartItems) {
            if (!item.getPlatformInfo().getUser().getId().equals(userId)) {
                throw new PlatformAccessDeniedException();
            }
        }
    }

    private int calculateTotalAmount(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToInt(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    private Platform extractPlatformFromCartItems(List<CartItem> cartItems) {
        String platformName = cartItems.get(0).getPlatformInfo().getPlatformName();
        return Platform.validatePlatform(platformName);
    }

    private OrderForm saveOrderForm(User user, int totalAmount, Platform platform) {
        OrderForm orderForm = OrderForm.builder()
                .user(user)
                .platform(platform)
                .totalAmount(totalAmount)
                .build();
        return orderFormRepository.save(orderForm);
    }

    private void saveOrderItems(OrderForm orderForm, List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = OrderItem.builder()
                    .orderForm(orderForm)
                    .itemName(cartItem.getItemName())
                    .itemUrl(cartItem.getItemUrl())
                    .quantity(cartItem.getQuantity())
                    .optionText(cartItem.getOptionText())
                    .price(cartItem.getPrice())
                    .build();
            orderItemRepository.save(orderItem);
        }
    }
    private void savePodUserMapping(Pod pod, User user, OrderForm orderForm) {
        PodUserMapping podUserMapping = PodUserMapping.updatePodUserMappingForOrderForm(
                pod,
                user,
                orderForm,
                IsApproved.PENDING, // 디폴트값 - 첫 주문서 신청 시, 팟장의 허락 전에는 PENDING
                PodRole.POD_MEMBER // 디폴트값 - 신청서 넣는 사람은 팟장이 아닌 팟원
        );
        podUserMappingRepository.save(podUserMapping);
    }

}
