package com.ccommit.fashionserver.dto;

import lombok.*;

/**
 * packageName    : com.ccommit.fashionserver.dto
 * fileName       : TossPaymentResponse
 * author         : juoiy
 * date           : 2024-01-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-01-27        juoiy       최초 생성
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TossPaymentResponse {
    private int code;
    private int status;
    private String payToken;
    private String checkoutPage;
    private String msg;
    private String errorCode;
    private int result;

}
