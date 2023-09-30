package com.ccommit.fashionserver.exception;

/**
 * packageName    : com.ccommit.fashionserver.exception
 * fileName       : DuplicateIdException
 * author         : juoiy
 * date           : 2023-09-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-27        juoiy       최초 생성
 */
public class FashionServerException extends RuntimeException {
    public Integer status;
    public FashionServerException(String message) {
        super(message);
    }

    public FashionServerException(String message, Integer status) {
        super(message);
        this.status = status;
    }
}
