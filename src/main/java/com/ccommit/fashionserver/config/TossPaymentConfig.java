package com.ccommit.fashionserver.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * packageName    : com.ccommit.fashionserver.config
 * fileName       : TossPaymentConfig
 * author         : juoiy
 * date           : 2023-10-14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-14        juoiy       최초 생성
 */
@Configuration
@Getter
public class TossPaymentConfig {
    @Value("${payment.toss.test_client_api_key}")
    private String clientApiKey;
    @Value("${payment.toss.test_secrete_api_key}")
    private String secretKey;
    @Value("${payment.toss.test_secrete_v2_api_key}")
    private String v2SecretKey;
    @Value("${payment.toss.payment_api_key}")
    private String tossPaymentApiKey;
    @Value("${payment.toss.payment_url}")
    private String tossPaymentUrl;
    //v2_payment_url
    @Value("${payment.toss.v2_payment_url}")
    private String tossV2PaymentUrl;
    @Value("${payment.toss.success_url}")
    private String successUrl;
    @Value("${payment.toss.fail_url}")
    private String failUrl;

}
