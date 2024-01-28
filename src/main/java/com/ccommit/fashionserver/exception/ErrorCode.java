package com.ccommit.fashionserver.exception;

import lombok.Getter;

/**
 * packageName    : com.ccommit.fashionserver.exception
 * fileName       : ErrorCode
 * author         : juoiy
 * date           : 2023-09-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-30        juoiy       최초 생성
 */
@Getter
public enum ErrorCode {
    USER_INSERT_DUPLICATE_ERROR(601, "중복된 아이디입니다. 확인해주세요."),
    USER_UPDATE_ERROR(602, "회원정보가 불일치합니다. 확인해주세요."),
    USER_NOT_AUTHORIZED_ERROR(603, "가입 권한이 없습니다."),
    USER_NOT_USING_ERROR(604, "회원 정보가 존재하지 않습니다. 확인해주세요."),
    LOGIN_ERROR(605, "권한에 맞는 로그인이 필요합니다."),

    PRODUCT_INSERT_ERROR(610, "상품 등록에 실패하였습니다."),
    PRODUCT_UPDATE_ERROR(611, "상품 수정에 실패하였습니다."),
    PRODUCT_DELETE_ERROR(612, "상품 삭제에 실패하였습니다."),
    PRODUCT_NOT_USING_ERROR(613, "상품 정보가 존재하지 않습니다. 확인해주세요."),
    PRODUCT_QUANTITY_NOT_ENOUGH_ERROR(614, "상품의 재고가 부족합니다. 확인해주세요."),

    CATEGORY_NOT_USING_ERROR(620, "존재하지 않는 카테고리입니다."),
    SEARCH_TYPE_NOT_USING_ERROR(621, "존재하지 않는 검색 타입입니다."),
    SEARCH_TYPE_NULL_ERROR(622, "검색 타입을 입력 후 다시 시도해주세요."),

    ORDER_INSERT_ERROR(630, "주문 등록에 실패하였습니다."),
    ORDER_ID_DUPLICATION_ERROR(631, "주문번호가 중복되었습니다. 다시 시도해주세요."),
    ORDER_NOT_USING_ERROR(632, "주문 정보가 존재하지 않습니다."),
    ORDER_CANCEL_POSSIBLE_DATE_NOT_USING_ERROR(633, "주문 취소 가능 날짜가 존재하지 않습니다. 확인해주세요."),
    ORDER_CANCEL_IMPOSSIBLE_ERROR(634, "주문 취소 가능한 날짜가 아닙니다. 확인해주세요."),
    ORDER_CANCEL_UPDATE_ERROR(635, "주문 취소에 실패하였습니다."),
    ORDER_UPDATE_ERROR(636, "주문 수정에 실패하였습니다."),


    CART_PRODUCT_NOT_USING_ERROR(640, "장바구니에 담긴 상품이 존재하지 않습니다. 확인해주세요."),

    CARD_PAYMENT_SUCCESS_ERROR(650, "카드 결제에 실패하였습니다. 다시 시도해주세요.(토스페이먼츠 API 확인바람)"),
    CARD_PAYMENT_INSERT_ERROR(651, "결제 정보 등록에 실패하였습니다. 확인해주세요."),
    CARD_PAYMENT_UPDATE_ERROR(652, "결제 정보 수정에 실패하였습니다. 확인해주세요."),
    CARD_PAYMENT_SELECT_ERROR(653, "결제 정보 조회에 실패하였습니다. 다시 시도해주세요."),
    PAYMENT_NOT_USING_ERROR(654, "결제 정보가 존재하지 않습니다."),

    TOSS_HTTP_SERVER_ERROR(660, "토스페이먼츠에 요청 중 문제가 발생하였습니다."),

    INPUT_NULL_ERROR(999, "필수 입력값이 없습니다. 확인해주세요.");

    // 에러 코드의 '코드 상태'을 반환한다.
    private final int status;

    // 에러 코드의 '코드 메시지'을 반환한다.
    private final String message;

    // 생성자 구성
    ErrorCode(final int status, final String message) {
        this.status = status;
        this.message = message;
    }
}
