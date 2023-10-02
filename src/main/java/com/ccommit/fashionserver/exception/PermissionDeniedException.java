package com.ccommit.fashionserver.exception;

/**
 * packageName    : com.ccommit.fashionserver.exception
 * fileName       : PermissionDeniedException
 * author         : juoiy
 * date           : 2023-09-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-27        juoiy       최초 생성
 */
public class PermissionDeniedException extends RuntimeException {
    public PermissionDeniedException(String message) {
        super(message);
    }
}
