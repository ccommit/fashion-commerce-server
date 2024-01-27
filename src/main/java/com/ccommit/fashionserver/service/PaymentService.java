package com.ccommit.fashionserver.service;

import com.ccommit.fashionserver.config.TossPaymentConfig;
import com.ccommit.fashionserver.dto.*;
import com.ccommit.fashionserver.exception.ErrorCode;
import com.ccommit.fashionserver.exception.FashionServerException;
import com.ccommit.fashionserver.mapper.PaymentMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * packageName    : com.ccommit.fashionserver.service
 * fileName       : PaymentService
 * author         : juoiy
 * date           : 2023-10-14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-14        juoiy       최초 생성
 */
@Log4j2
@Service
public class PaymentService {
    @Autowired
    private final PaymentMapper paymentMapper;

    @Autowired
    TossPaymentConfig tossPaymentConfig;

    public PaymentService(PaymentMapper paymentMapper) {
        this.paymentMapper = paymentMapper;
    }

    private HttpHeaders getHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", tossPaymentConfig.getTossPaymentApiKey());
        return httpHeaders;
    }

    public TossPaymentResponse createPayment(TossPaymentRequest tossPaymentRequest) {
        HttpHeaders httpHeaders = getHeader();
        RestTemplate restTemplate = new RestTemplate();
        String uuid = UUID.randomUUID().toString();
        String orderId = uuid.replaceAll("-", "").substring(0, 10);

        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", tossPaymentRequest.getOrderNo());
        params.put("amount", tossPaymentRequest.getAmount());
        params.put("amountTaxFree", 0);
        params.put("productDesc", tossPaymentRequest.getProductDesc());
        params.put("apiKey", tossPaymentConfig.getV2SecretKey());
        params.put("retUrl", tossPaymentConfig.getSuccessUrl());
        params.put("retCancelUrl", tossPaymentConfig.getFailUrl());
        params.put("autoExecute", false);

        HttpEntity<Map<String, Object>> requestData = new HttpEntity<>(params, httpHeaders);
        URI uri = URI.create(tossPaymentConfig.getTossV2PaymentUrl() +"/payments");
        ResponseEntity<TossPaymentResponse> responseEntity ;

        try{
            responseEntity = restTemplate.exchange(uri.toString(), HttpMethod.POST, requestData, TossPaymentResponse.class);
            log.info("토큰키 : "+responseEntity.getBody().getPayToken());
        }catch (HttpClientErrorException e){
            throw new FashionServerException(ErrorCode.valueOf("HTTP_SERVER_ERROR").getMessage() + ", 토스페이먼츠 응답: " + e.getMessage(), 660);
        }
        return responseEntity.getBody();
    }

    //TODO: 테스트
    public TossPaymentResponse approvePayment(String orderNo) {
        /** execute ( 결제 생성-인증-인증완 을 알려준다 ) */
        HttpHeaders httpHeaders = getHeader();
        Map<String, Object> params = new HashMap<>();

        params.put("apiKey", tossPaymentConfig.getV2SecretKey());
        params.put("payToken", orderNo);
        HttpEntity<Map<String, Object>> requestData = new HttpEntity<>(params, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        URI uri = URI.create(tossPaymentConfig.getTossV2PaymentUrl() + "/execute");
        ResponseEntity<TossPaymentResponse> responseEntity;
        try {
            responseEntity = restTemplate.exchange(uri.toString(), HttpMethod.POST, requestData, TossPaymentResponse.class);
        } catch (HttpClientErrorException e) {
            throw new FashionServerException(ErrorCode.valueOf("HTTP_SERVER_ERROR").getMessage() + ", 토스페이먼츠 응답: " + e.getMessage(), 660);
        }
        return responseEntity.getBody();
    }
    //TODO: 환불하기
    public TossPaymentResponse refundsPayment(TossPaymentRequest tossPaymentRequest){
        TossPaymentResponse tossPaymentResponse = new TossPaymentResponse();

        return tossPaymentResponse;
    }

    //TODO: 결제 조회
    public TossPaymentResponse selectPayment(TossPaymentRequest tossPaymentRequest){
        TossPaymentResponse tossPaymentResponse = new TossPaymentResponse();

        return tossPaymentResponse;
    }

}
