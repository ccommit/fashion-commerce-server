package com.ccommit.fashionserver.dto.status;

/**
 * packageName    : com.ccommit.fashionserver.dto
 * fileName       : OrderStatus
 * author         : juoiy
 * date           : 2023-10-04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-04        juoiy       최초 생성
 */
public enum PaymentStatus {
    PAY_STANDBY,                // 결제 대기 중
    PAY_APPROVED,               // 구매자 인증 완료
    PAY_CANCEL,                 // 결제 취소
    PAY_PROGRESS,               // 결제 진행 중
    PAY_COMPLETE,               // 결제 완료
    REFUND_PROGRESS,            // 환불 진행 중
    REFUND_SUCCESS,             // 환불 성공
    SETTLEMENT_COMPLETE,        // 정산 완료
    SETTLEMENT_REFUND_COMPLETE  // 환불 정산 완료
}


