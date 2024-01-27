package com.ccommit.fashionserver.dto;

import lombok.*;

/**
 * packageName    : com.ccommit.fashionserver.dto
 * fileName       : TossRequest
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
public class TossPaymentRequest {
    private String orderNo;
    private int amount;
    private int amountTaxFree; //비과세 금액
    private String productDesc;
    private String apiKey;
    private String retUrl;
    private String retCancelUrl;
    private boolean autoExecute; // 자동 승인 설정 (필수)

}
