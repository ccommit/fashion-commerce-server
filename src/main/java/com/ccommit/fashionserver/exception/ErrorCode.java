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

    ORDER_INSERT_ERROR(630, "주문 등록에 실패하였습니다."),
    ORDER_NUMBER_DUPLICATION_ERROR(631, "주문번호가 중복되었습니다. 다시 시도해주세요."),

    CART_PRODUCT_NOT_USING_ERROR(640, "장바구니에 담긴 상품이 존재하지 않습니다. 확인해주세요."),

    INPUT_NULL_ERROR(999, "필수 입력값이 없습니다. 확인해주세요."),

    NOT_AUTHORIZED_ERROR(777, "권한이 없습니다.");

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
