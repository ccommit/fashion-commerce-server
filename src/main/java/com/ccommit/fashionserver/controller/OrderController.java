package com.ccommit.fashionserver.controller;

import com.ccommit.fashionserver.dto.OrderDto;
import com.ccommit.fashionserver.dto.ProductDto;
import com.ccommit.fashionserver.service.OrderService;
import com.ccommit.fashionserver.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * packageName    : com.ccommit.fashionserver.controller
 * fileName       : OrderController
 * author         : juoiy
 * date           : 2023-09-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-27        juoiy       최초 생성
 */
@Log4j2
@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private final OrderService orderService;
    @Autowired
    private final ProductService productService;

    public OrderController(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
    }
    //TODO: merge 후 logingcheck, exception 처리 추가하기
    //TODO:

    @PostMapping("/{userId}")
    public OrderDto insertOrder(@PathVariable("userId") int userId, @RequestBody List<ProductDto> orderProductIdList) throws JsonProcessingException {
        if (orderProductIdList.size() == 0)
            throw new RuntimeException("주문상품을 선택하지않았습니다. 확인해주세요.");
        for (int i = 0; i < orderProductIdList.size(); i++) {
            int productId = orderProductIdList.get(i).getId();
            if (productService.detailProduct(productId) == null) {
                log.info(productId + "상품이 존재하지 않습니다.");
                throw new RuntimeException("상품이 존재하지 않습니다.");
            }
        }
        OrderDto orderDto = orderService.insertOrder(userId, orderProductIdList);
        return orderDto;
    }

    // TODO: 주문 취소 (취소가능 조건: 상태가 주문접수,완료일 경우에만 가능하다. 결제 중 결제 완료는 환불)
    @PatchMapping("")
    public void cancelOrder() {

    }

    // TODO: 장바구니 목록 조회
    public void getCartList() {
        // 상품 요청
        // cache에 존재하는지 확인
        // 존재하면 cache에서 가져오기
        // 존재하지 않으면 api호출?
        // api호출 후 cache에 저장

    }

}
