package com.podmate.global.common.code.status;

import com.podmate.global.common.code.BaseErrorCode;
import com.podmate.global.common.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // --- Common ---
    _INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON400", "입력값이 올바르지 않습니다"),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "권한이 없습니다."),
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // --- Token ---
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "TOKEN_404", "리프레시 토큰을 찾을 수 없습니다."),
    TOKEN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "TOKEN_401", "리프레시 토큰 인증 실패입니다."),

    // --- USER ---
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_404", "유저를 찾을 수 없습니다."),

    // --- POD ---
    POD_NOT_FOUND(HttpStatus.NOT_FOUND, "POD_404", "팟을 찾을 수 없습니다."),
    POD_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "POD_404", "팟 타입을 찾을 수 없습니다."),
    POD_STATUS_MISMATCH(HttpStatus.BAD_REQUEST, "POD_400", "팟 상태 값이 일치하지 않습니다."),
    PENDING_ORDER_MISMATCH(HttpStatus.BAD_REQUEST, "POD_400", "ORDER_COMPLETED로 변경하려면 InprogressStatus가 PENDING_ORDER여야 합니다."),
    INVALID_STATUS_EXCEPTION(HttpStatus.BAD_REQUEST, "POD_400", "유효하지 않은 팟 진행 상태입니다."),

    // --- PODUSERMAPPING ---
    POD_USER_MAPPING_NOT_FOUND(HttpStatus.NOT_FOUND, "PODUSERMAPPING_404", "팟ID에 맞는 유저를 찾을 수 없습니다."),
    POD_LEADER_NOT_FOUND(HttpStatus.NOT_FOUND, "PODUSERMAPPING_404", "팟ID에 맞는 리더를 찾을 수 없습니다."),
    POD_LEADER_USER_MISMATCH(HttpStatus.BAD_REQUEST, "PODUSERMAPPING_400", "해당 팟의 리더가 주어진 사용자와 일치하지 않습니다."),
    POD_STATUS_NOT_RECRUITING(HttpStatus.BAD_REQUEST, "PODUSERMAPPING_400", "모집중인 팟만 수락/거절할 수 있습니다."),

    // --- ADDRESS ---
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "ADDRESS_404", "주소를 찾을 수 없습니다."),

    // --- DELIVERY ---
    DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "DELIVERY_404", "팟ID에 맞는 배송 정보를 찾을 수 없습니다."),
    SHIPPING_MISMATCH(HttpStatus.BAD_REQUEST, "DELIVERY_400", "DELIVERED 변경하려면 DeliveryStatus가 SHIPPING여야 합니다."),

    // --- JJIM ---
    DUPLICATE_JJIM(HttpStatus.CONFLICT, "JJIM_409", "이미 찜한 팟입니다."),

    // --- PLATFORMINFO --
    PLATFORM_NOT_SUPPORTED(HttpStatus.NOT_FOUND, "PLATFORM_404", "지원하지 않는 플랫폼입니다."),
    PLATFORM_ACCESS_DENIED_EXCEPTION(HttpStatus.FORBIDDEN, "PLATFORM_403", "해당 장바구니는 본인의 것이 아닙니다."),

    // --- REVIEW ---
    INVALID_REVIEW_OPTION_TEXT(HttpStatus.NOT_FOUND, "REVIEW_400", "유효하지 않은 리뷰 옵션입니다."),
    REVIEW_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW_404", "리뷰 옵션을 찾을 수 없습니다."),

    // --- CARTITEM --
    CARTITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "CARTITEM_404", "장바구니 상품을 찾을 수 없습니다."),

    // --- ORDERFORM ---
    ORDERFORM_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDERFORM_404", "해당 주문서를 찾을 수 없습니다."),
    ORDERFORM_ACCESS_DENIED_EXCEPTION(HttpStatus.FORBIDDEN, "ORDERFORM_403", "해당 주문서는 본인의 것이 아닙니다."),

    // --- NOTIFICATION --
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTIFICATION", "알림을 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;



    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
