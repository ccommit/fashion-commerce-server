package com.ccommit.fashionserver.controller;

import com.ccommit.fashionserver.aop.CommonResponse;
import com.ccommit.fashionserver.aop.LoginCheck;
import com.ccommit.fashionserver.config.TossPaymentConfig;
import com.ccommit.fashionserver.dto.PaymentDto;
import com.ccommit.fashionserver.dto.PaymentReq;
import com.ccommit.fashionserver.dto.PaymentRes;
import com.ccommit.fashionserver.exception.ErrorCode;
import com.ccommit.fashionserver.exception.FashionServerException;
import com.ccommit.fashionserver.service.PaymentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * packageName    : com.ccommit.fashionserver.controller
 * fileName       : PaymentController
 * author         : juoiy
 * date           : 2023-10-14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-14        juoiy       최초 생성
 */
@Log4j2
@RestController
@RequestMapping("/payments")
public class PaymentsController {
    @Autowired
    private final PaymentService paymentService;

    @Autowired
    private final TossPaymentConfig tossPaymentConfig;

    public PaymentsController(PaymentService paymentService, TossPaymentConfig tossPaymentConfig) {
        this.paymentService = paymentService;
        this.tossPaymentConfig = tossPaymentConfig;
    }

    @PostMapping("/key-in")
    @LoginCheck(types = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<PaymentRes>> cardPaymentKeyIn(Integer loginSession, @RequestBody PaymentReq paymentReq) {
        PaymentRes result = paymentService.cardPayment(paymentReq);
        CommonResponse<PaymentRes> response = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "카드결제가 성공하였습니다.", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    @LoginCheck(types = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<PaymentRes>> getPaymentHistory(Integer loginSession, @PathVariable("orderId") String orderId) {
        PaymentRes result = paymentService.getPaymentHistory(orderId);
        CommonResponse<PaymentRes> response = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "카드결제 조회에 성공하였습니다.", result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancel")
    @LoginCheck(types = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<PaymentRes>> paymentCancel(Integer loginSession, @RequestBody PaymentDto paymentDto) {
        if (paymentDto.getPaymentKey() == null || paymentDto.getCancelReason() == null)
            throw new FashionServerException(ErrorCode.valueOf("INPUT_NULL_ERROR").getMessage(), 999);
        paymentService.paymentCancel(paymentDto);
        CommonResponse<PaymentRes> response = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "결제 취소가 되었습니다.", null);
        return ResponseEntity.ok(response);
    }

}
