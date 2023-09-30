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
    USER_UPDATE_ERROR(602,  "회원정보가 불일치합니다. 확인해주세요."),
    USER_NOT_AUTHORIZED_ERROR(603,  "가입 권한이 없습니다."),
    USER_NOT_USING_ERROR(604,  "회원 정보가 존재하지 않습니다. 확인해주세요."),
    ;

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
