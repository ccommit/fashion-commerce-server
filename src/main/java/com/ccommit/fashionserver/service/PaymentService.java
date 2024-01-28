package com.ccommit.fashionserver.service;

import com.ccommit.fashionserver.config.TossPaymentConfig;
import com.ccommit.fashionserver.dto.PaymentDto;
import com.ccommit.fashionserver.dto.TossPaymentRequest;
import com.ccommit.fashionserver.dto.TossPaymentResponse;
import com.ccommit.fashionserver.dto.status.PaymentStatus;
import com.ccommit.fashionserver.exception.ErrorCode;
import com.ccommit.fashionserver.exception.FashionServerException;
import com.ccommit.fashionserver.mapper.OrderMapper;
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
    private final OrderMapper orderMapper;

    @Autowired
    TossPaymentConfig tossPaymentConfig;
    final Integer RESPONSE_SUCCESS_CODE = 0;
    final Integer RESPONSE_FAIL_CODE = -1;

    public PaymentService(PaymentMapper paymentMapper, OrderMapper orderMapper) {
        this.paymentMapper = paymentMapper;
        this.orderMapper = orderMapper;
    }

    private HttpHeaders getHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", tossPaymentConfig.getTossPaymentApiKey());
        return httpHeaders;
    }

    public void tossPaymentFail(String orderNo) {
        String getPaymentKey = paymentMapper.getPaymentInfo(orderNo).getPaymentKey();
        PaymentDto paymentDto = PaymentDto.builder()
                .status(String.valueOf(PaymentStatus.PAY_CANCEL))
                .paymentKey(getPaymentKey)
                .build();
        if (paymentMapper.updatePaymentInfo(paymentDto) == 0) {
            throw new FashionServerException(ErrorCode.valueOf("CARD_PAYMENT_UPDATE_ERROR").getMessage(), 652);
        }
    }

    public TossPaymentResponse createPayment(TossPaymentRequest tossPaymentRequest) {
        HttpHeaders httpHeaders = getHeader();
        RestTemplate restTemplate = new RestTemplate();

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
        URI uri = URI.create(tossPaymentConfig.getTossV2PaymentUrl() + "/payments");
        ResponseEntity<TossPaymentResponse> responseEntity;

        try {
            responseEntity = restTemplate.exchange(uri.toString(), HttpMethod.POST, requestData, TossPaymentResponse.class);
            String tokenKey = responseEntity.getBody().getPayToken();
            int code = responseEntity.getBody().getCode();

            if (code == RESPONSE_SUCCESS_CODE) {
                PaymentDto paymentDto = PaymentDto.builder()
                        .status(String.valueOf(PaymentStatus.PAY_APPROVED))
                        .orderId(tossPaymentRequest.getOrderNo())
                        .cardNumber("5388032333580235")
                        .paymentKey(tokenKey)
                        .build();
                paymentMapper.insertPaymentInfo(paymentDto);
            }
            if (code == RESPONSE_FAIL_CODE) {
                throw new FashionServerException(responseEntity.getBody().getMsg(), responseEntity.getBody().getStatus());
            }
        } catch (HttpClientErrorException e) {
            throw new FashionServerException(ErrorCode.valueOf("TOSS_HTTP_SERVER_ERROR").getMessage() + ", 토스페이먼츠 응답: " + e.getMessage(), 660);
        }
        return responseEntity.getBody();
    }

    public TossPaymentResponse approvePayment(String orderNo) {
        HttpHeaders httpHeaders = getHeader();
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> params = new HashMap<>();

        String tokenKey = paymentMapper.getPaymentInfo(orderNo).getPaymentKey();
        params.put("apiKey", tossPaymentConfig.getV2SecretKey());
        params.put("payToken", tokenKey);

        HttpEntity<Map<String, Object>> requestData = new HttpEntity<>(params, httpHeaders);
        URI uri = URI.create(tossPaymentConfig.getTossV2PaymentUrl() + "/execute"); // 결제 승인하기
        ResponseEntity<TossPaymentResponse> responseEntity;
        try {
            responseEntity = restTemplate.exchange(uri.toString(), HttpMethod.POST, requestData, TossPaymentResponse.class);
            int code = responseEntity.getBody().getCode(); // code=0 과 -1로 SUCCESS, FAIL 로 구분합니다.
            if (code == RESPONSE_SUCCESS_CODE) {
                PaymentDto paymentDto = PaymentDto.builder()
                        .status(String.valueOf(PaymentStatus.PAY_COMPLETE))
                        .paymentKey(tokenKey)
                        .build();
                if (paymentMapper.updatePaymentInfo(paymentDto) == 0) { // payment 테이블에 결제상태 값(결제완료) UPDATE
                    throw new FashionServerException(ErrorCode.valueOf("CARD_PAYMENT_UPDATE_ERROR").getMessage(), 652);
                }
                //orderMapper.insertOrder() 마지막에 주문 테이블에 주문정보를 insert가 맞는지 헷갈려서 주석으로 적어보았습니다.
            }
        } catch (HttpClientErrorException e) {
            throw new FashionServerException(ErrorCode.valueOf("TOSS_HTTP_SERVER_ERROR").getMessage() + ", 토스페이먼츠 응답: " + e.getMessage(), 660);
        }
        return responseEntity.getBody();
    }

    //TODO: 환불하기
    public TossPaymentResponse refundsPayment(TossPaymentRequest tossPaymentRequest) {
        TossPaymentResponse tossPaymentResponse = new TossPaymentResponse();

        return tossPaymentResponse;
    }

    //TODO: 결제 조회
    public TossPaymentResponse selectPayment(TossPaymentRequest tossPaymentRequest) {
        TossPaymentResponse tossPaymentResponse = new TossPaymentResponse();

        return tossPaymentResponse;
    }

}
