package com.ccommit.fashionserver.aop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * packageName    : com.ccommit.fashionserver.utils
 * fileName       : CommonRes
 * author         : juoiy
 * date           : 2023-09-16
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-16        juoiy       최초 생성
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {
    private HttpStatus httpStatus;
    private String code;
    private String message;
    private T requestBody;

}
