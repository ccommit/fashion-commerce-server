package com.ccommit.fashionserver.service;

import com.ccommit.fashionserver.config.TossPaymentConfig;
import com.ccommit.fashionserver.dto.PaymentDto;
import com.ccommit.fashionserver.dto.PaymentReq;
import com.ccommit.fashionserver.dto.PaymentRes;
import com.ccommit.fashionserver.dto.PaymentStatus;
import com.ccommit.fashionserver.exception.ErrorCode;
import com.ccommit.fashionserver.exception.FashionServerException;
import com.ccommit.fashionserver.mapper.PaymentMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
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
    private final TossPaymentConfig tossPaymentConfig;

    public PaymentService(PaymentMapper paymentMapper, TossPaymentConfig tossPaymentConfig) {
        this.paymentMapper = paymentMapper;
        this.tossPaymentConfig = tossPaymentConfig;
    }

    public PaymentRes cardPayment(PaymentReq paymentReq) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", tossPaymentConfig.TOSS_PAYMENT_API_KEY);

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentReq.getAmount());
        params.put("cardExpirationMonth", paymentReq.getCardExpirationMonth());
        params.put("cardExpirationYear", paymentReq.getCardExpirationYear());
        params.put("cardNumber", paymentReq.getCardNumber());
        params.put("customerIdentityNumber", paymentReq.getCustomerIdentityNumber());
        params.put("orderId", paymentReq.getOrderId());
        params.put("orderName", paymentReq.getOrderName());

        HttpEntity<Map<String, Object>> requestData = new HttpEntity<>(params, httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        URI uri = URI.create(tossPaymentConfig.TOSS_PAYMENT_URL + "/key-in");
        ResponseEntity<PaymentRes> responseEntity = restTemplate.exchange(uri.toString(), HttpMethod.POST, requestData, PaymentRes.class);
        if (responseEntity.getStatusCodeValue() != 200) {
            throw new FashionServerException(ErrorCode.valueOf("CARD_PAYMENT_SUCCESS_ERROR").getMessage(), responseEntity.getStatusCodeValue());
        } else {
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setPaymentKey(responseEntity.getBody().getPaymentKey());
            paymentDto.setCardNumber(responseEntity.getBody().getOrderId());
            paymentDto.setStatus(PaymentStatus.PAYMENT_COMPLETE.getPaymentCode());
            log.info("paymentDto : " + paymentDto.getPaymentKey());
            int result = paymentMapper.insertPaymentInfo(paymentDto);
            if (result == 0)
                throw new FashionServerException(ErrorCode.valueOf("CARD_PAYMENT_INSERT_ERROR").getMessage(), 651);
        }
        return responseEntity.getBody();
    }

    public PaymentRes getPaymentHistory(String orderId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", tossPaymentConfig.TOSS_PAYMENT_API_KEY);

        HttpEntity<String> requestData = new HttpEntity<>(httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        URI uri = URI.create(tossPaymentConfig.TOSS_PAYMENT_URL + "/orders/" + orderId);
        ResponseEntity<PaymentRes> responseEntity = restTemplate.exchange(uri.toString(), HttpMethod.GET, requestData, PaymentRes.class);
        if (responseEntity.getStatusCodeValue() != 200)
            throw new FashionServerException(ErrorCode.valueOf("CARD_PAYMENT_SELECT_ERROR").getMessage(), 653);
        return responseEntity.getBody();
    }

    public PaymentRes paymentCancel(PaymentDto paymentDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", tossPaymentConfig.TOSS_PAYMENT_API_KEY);

        Map<String, Object> params = new HashMap<>();
        params.put("cancelReason", paymentDto.getCancelReason());

        HttpEntity<Map<String, Object>> requestData = new HttpEntity<>(params, httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        URI uri = URI.create(tossPaymentConfig.TOSS_PAYMENT_URL + "/" + paymentDto.getPaymentKey() + "/cancel");
        ResponseEntity<PaymentRes> responseEntity = restTemplate.exchange(uri.toString(), HttpMethod.POST, requestData, PaymentRes.class);
        if (responseEntity.getStatusCodeValue() == 200) {
            paymentDto.setStatus(PaymentStatus.PAYMENT_CANCEL.getPaymentCode());
            int result = paymentMapper.updatePaymentCancel(paymentDto);
            if (result == 0)
                throw new FashionServerException(ErrorCode.valueOf("CARD_PAYMENT_UPDATE_ERROR").getMessage(), 652);
        }
        return responseEntity.getBody();
    }
}
