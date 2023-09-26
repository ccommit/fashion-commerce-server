package com.ccommit.fashionserver.aop;

import com.ccommit.fashionserver.exception.DuplicateException;
import com.ccommit.fashionserver.exception.NotMatchException;
import com.ccommit.fashionserver.exception.PermissionDeniedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * packageName    : com.ccommit.fashionserver.aop
 * fileName       : GlobalExceptionHandler
 * author         : juoiy
 * date           : 2023-09-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-07        juoiy       최초 생성
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NullPointerException.class)
    private ResponseEntity<Object> handleNotContent(NullPointerException ex, HttpServletRequest request) {
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "ERR_000", ex.getMessage(), request.getServletPath());
        return new ResponseEntity<>(commonResponse, new HttpHeaders(), commonResponse.getHttpStatus());
    }

    @ExceptionHandler(value = DuplicateException.class)
    private ResponseEntity<Object> handleDuplicate(DuplicateException ex, HttpServletRequest request) {
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "ERR_001", ex.getMessage(), request.getServletPath());
        return new ResponseEntity<>(commonResponse, new HttpHeaders(), commonResponse.getHttpStatus());
    }

    @ExceptionHandler(value = NotMatchException.class)
    private ResponseEntity<Object> handleNotMatching(NotMatchException ex, HttpServletRequest request) {
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "ERR_002", ex.getMessage(), request.getServletPath());
        return new ResponseEntity<>(commonResponse, new HttpHeaders(), commonResponse.getHttpStatus());
    }

    @ExceptionHandler(value = PermissionDeniedException.class)
    private ResponseEntity<Object> handlePermissionDenied(PermissionDeniedException ex, HttpServletRequest request) {
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "ERR_003", ex.getMessage(), request.getServletPath());
        return new ResponseEntity<>(commonResponse, new HttpHeaders(), commonResponse.getHttpStatus());
    }
}
