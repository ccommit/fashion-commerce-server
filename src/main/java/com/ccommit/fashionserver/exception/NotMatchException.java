package com.ccommit.fashionserver.exception;

/**
 * packageName    : com.ccommit.fashionserver.exception
 * fileName       : NotMatchCategoryIdException
 * author         : juoiy
 * date           : 2023-09-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-07        juoiy       최초 생성
 */
public class NotMatchException extends RuntimeException {
    public NotMatchException(String message) {
        super(message);
    }
}
