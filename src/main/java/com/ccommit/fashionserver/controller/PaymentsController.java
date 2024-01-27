package com.ccommit.fashionserver.controller;

import com.ccommit.fashionserver.aop.CommonResponse;
import com.ccommit.fashionserver.aop.LoginCheck;
import com.ccommit.fashionserver.dto.*;
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

    public PaymentsController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    //TODO: 결제 요청
    @GetMapping("")
    public ResponseEntity<CommonResponse<TossPaymentResponse>> createPayment(@RequestBody TossPaymentRequest tossPaymentRequest) {
        TossPaymentResponse result = paymentService.createPayment(tossPaymentRequest);

        CommonResponse<TossPaymentResponse> response = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "결제 요청 성공하였습니다.", result);
        return ResponseEntity.ok(response);
    }

    //TODO: 결제 성공
    @GetMapping("/toss/success")
    public ResponseEntity<CommonResponse<TossPaymentResponse>> tossPaymentSuccess(@RequestParam String status, @RequestParam String orderNo,
                                                                                  @RequestParam String payMethod, @RequestParam String bankCode) {
        PaymentResponse result = new PaymentResponse();
        paymentService.approvePayment(orderNo);
        CommonResponse<TossPaymentResponse> response = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "결제에 성공하였습니다.", null);
        return ResponseEntity.ok(response);
    }

    //TODO: 결제 요청 실패
    @GetMapping("/toss/fail")
    public ResponseEntity<CommonResponse<TossPaymentResponse>> tossPaymentFail(@RequestParam String status, @RequestParam String orderNo,
                                                                               @RequestParam String payMethod, @RequestParam String bankCode) {

        CommonResponse<TossPaymentResponse> response = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "결제 실패하였습니다.", null);
        return ResponseEntity.ok(response);
    }

    //TODO: 환불하기
    @PostMapping("/refunds/{orderNo}")
    public ResponseEntity<CommonResponse<TossPaymentResponse>> refundsPayment(@RequestBody TossPaymentRequest tossPaymentRequest) {
        TossPaymentResponse result = paymentService.refundsPayment(tossPaymentRequest);

        CommonResponse<TossPaymentResponse> response = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "결제 요청 성공하였습니다.", result);
        return ResponseEntity.ok(response);
    }

    //TODO: 결제 조회하기
    @GetMapping("/select")
    public ResponseEntity<CommonResponse<TossPaymentResponse>> selectPayment(@RequestBody TossPaymentRequest tossPaymentRequest) {
        TossPaymentResponse result = paymentService.refundsPayment(tossPaymentRequest);

        CommonResponse<TossPaymentResponse> response = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "결제 요청 성공하였습니다.", result);
        return ResponseEntity.ok(response);
    }

}
