package com.ccommit.fashionserver.controller;

import com.ccommit.fashionserver.aop.CommonResponse;
import com.ccommit.fashionserver.aop.LoginCheck;
import com.ccommit.fashionserver.dto.OrderDto;
import com.ccommit.fashionserver.dto.ProductDto;
import com.ccommit.fashionserver.dto.RequestProductDto;
import com.ccommit.fashionserver.exception.ErrorCode;
import com.ccommit.fashionserver.exception.FashionServerException;
import com.ccommit.fashionserver.service.OrderService;
import com.ccommit.fashionserver.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private StringRedisTemplate redisTemplate;

    public OrderController(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
    }

    @LoginCheck(types = LoginCheck.UserType.USER)
    @PostMapping("")
    public ResponseEntity<CommonResponse<OrderDto>> insertOrder(Integer userId, @RequestBody RequestProductDto orderProductList) throws JsonProcessingException {
        if (orderProductList.getProductDtoList().size() == 0 || orderProductList.getProductDtoList() == null)
            throw new FashionServerException(ErrorCode.valueOf("PRODUCT_NOT_USING_ERROR").getMessage(), 613);
        OrderDto orderDto = orderService.insertOrder(userId, orderProductList);
        CommonResponse<OrderDto> response = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "상품 주문에 성공하였습니다.", orderDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    @LoginCheck(types = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<List<OrderDto>>> getUserOrderList(Integer userId) throws ParseException {
        List<OrderDto> orderDtoList = orderService.getUserOrderList(userId);
        CommonResponse<List<OrderDto>> response = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "주문 목록 조회에 성공하였습니다.", orderDtoList);
        return ResponseEntity.ok(response);
    }

    // TODO: 주문 취소 (취소가능 조건: 상태가 주문접수,완료일 경우에만 가능하다. 결제 중 결제 완료는 환불)
    @PatchMapping("")
    public void cancelOrder() {

    }

    @GetMapping("/{userId}/carts")
    @LoginCheck(types = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<List<ProductDto>>> addCartList(Integer loginSession, @PathVariable("userId") int userId, @RequestBody RequestProductDto orderProductList) {
        if (loginSession != userId)
            throw new FashionServerException(ErrorCode.valueOf("USER_UPDATE_ERROR").getMessage(), 602);
        if (orderProductList.getProductDtoList().size() == 0 || orderProductList.getProductDtoList() == null)
            throw new FashionServerException(ErrorCode.valueOf("PRODUCT_NOT_USING_ERROR").getMessage(), 613);
        List<ProductDto> productDtoList = orderService.addCartList(userId, orderProductList);
        CommonResponse<List<ProductDto>> response = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "장바구니에 상품 담기 성공하였습니다.", productDtoList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/carts/put")
    @LoginCheck(types = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<List<ProductDto>>> putCartList(Integer loginSession, @PathVariable("userId") int userId, @RequestBody RequestProductDto orderProductList) {
        if (loginSession != userId)
            throw new FashionServerException(ErrorCode.valueOf("USER_UPDATE_ERROR").getMessage(), 602);
        if (orderProductList.getProductDtoList().size() == 0 || orderProductList.getProductDtoList() == null)
            throw new FashionServerException(ErrorCode.valueOf("PRODUCT_NOT_USING_ERROR").getMessage(), 613);
        List<ProductDto> productDtoList = orderService.putCartList(userId, orderProductList);
        CommonResponse<List<ProductDto>> response = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "장바구니에 상품 수정을 성공하였습니다.", productDtoList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/carts/list")
    @LoginCheck(types = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<List<ProductDto>>> getCartList(Integer userId) throws ParseException {
        List<ProductDto> productDtoList = orderService.getCartList(userId);
        CommonResponse<List<ProductDto>> response = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "장바구니 목록 조회 성공", productDtoList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{userId}/carts")
    @LoginCheck(types = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<OrderDto>> cartOrder(Integer loginSession, @PathVariable("userId") int userId) throws ParseException, JsonProcessingException {
        if (loginSession != userId)
            throw new FashionServerException(ErrorCode.valueOf("USER_UPDATE_ERROR").getMessage(), 602);
        OrderDto orderDto = orderService.cartOrder(userId);
        CommonResponse<OrderDto> response = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "장바구니 상품 구매 성공하였습니다.", orderDto);
        return ResponseEntity.ok(response);
    }

}
