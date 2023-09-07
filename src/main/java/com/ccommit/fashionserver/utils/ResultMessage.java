package com.ccommit.fashionserver.utils;

import lombok.Getter;
import lombok.ToString;

/**
 * packageName    : com.ccommit.fashionserver.utils
 * fileName       : CommonResponse
 * author         : juoiy
 * date           : 2023-09-05
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-05        juoiy       최초 생성
 */
@Getter
@ToString
public enum ResultMessage {
    SUCCESS("정상적으로 처리되었습니다.", 1),
    FAIL("처리에 실패하였습니다.", 0);

    private final int code;
    private final String message;

    ResultMessage(String message, int code) {
        this.message = message;
        this.code = code;
    }

}
