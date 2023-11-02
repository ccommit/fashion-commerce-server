package com.ccommit.fashionserver.service;

import com.ccommit.fashionserver.dto.OrderDto;
import com.ccommit.fashionserver.dto.ProductDto;
import com.ccommit.fashionserver.dto.ProductInfoDto;
import com.ccommit.fashionserver.exception.ErrorCode;
import com.ccommit.fashionserver.exception.FashionServerException;
import com.ccommit.fashionserver.mapper.OrderMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : com.ccommit.fashionserver.service
 * fileName       : OrderService
 * author         : juoiy
 * date           : 2023-09-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-27        juoiy       최초 생성
 */
@Log4j2
@Service
public class OrderService {
    @Autowired
    private final OrderMapper orderMapper;
    @Autowired
    private final ProductService productService;

    @Autowired
    private final StringRedisTemplate redisTemplate;

    public OrderService(OrderMapper orderMapper, ProductService productService, StringRedisTemplate redisTemplate) {
        this.orderMapper = orderMapper;
        this.productService = productService;
        this.redisTemplate = redisTemplate;
    }

    public OrderDto insertOrder(int userId, ProductDto orderProductList) throws JsonProcessingException {
        OrderDto orderDto = new OrderDto();
        ObjectMapper objectMapper = new ObjectMapper();
        int orderTotalPrice = 0;
        ArrayList<ProductInfoDto> productInfoDtoList = new ArrayList<>();
        for (int i = 0; i < orderProductList.getProductDtoList().size(); i++) {
            ProductDto productDto = productService.getDetailProduct(orderProductList.getProductDtoList().get(i).getId());
            int productQuantity = productDto.getSaleQuantity();
            int orderQuantity = orderProductList.getProductDtoList().get(i).getSaleQuantity();
            if (productQuantity < 1 || productQuantity < orderQuantity)
                throw new FashionServerException(ErrorCode.valueOf("PRODUCT_QUANTITY_NOT_ENOUGH_ERROR").getMessage(), 614);

            int resultQuantity = productQuantity - orderQuantity;
            log.debug("상품수량 - 주문수량 = 차감결과수량 : " + productQuantity + " - " + orderQuantity + " = " + resultQuantity);

            int updateResult = orderMapper.updateSaleQuantity(resultQuantity, orderProductList.getProductDtoList().get(i).getId());
            if (updateResult == 0)
                throw new FashionServerException(ErrorCode.valueOf("PRODUCT_UPDATE_ERROR").getMessage(), 611);

            int orderPrice = orderQuantity * (productDto.getPrice());
            orderTotalPrice += orderPrice;
            log.debug("productId: " + productDto.getId() + ", orderQuantity: " + orderQuantity + ",price: " + productDto.getPrice()
                    + ", orderPrice: " + orderPrice + ", orderTotalPrice: " + orderTotalPrice);
            ProductInfoDto productInfoDto = ProductInfoDto.builder()
                    .id(productDto.getId())
                    .saleQuantity(orderQuantity)
                    .name(productDto.getName())
                    .price(productDto.getPrice())
                    .build();
            productInfoDtoList.add(productInfoDto);
        }
        orderDto.setTotalPrice(orderTotalPrice);
        String json = objectMapper.writeValueAsString(productInfoDtoList);
        orderDto.setProductInfo(json);
        orderDto.setUserId(userId);
        final int LENGTH = 20; // 주문번호 길이 제한
        String orderNumber = RandomStringUtils.randomAlphanumeric(LENGTH);
        orderDto.setOrderNumber(orderNumber);
        int isExistOrderNumber = orderMapper.isExistOrderNumber(orderDto.getOrderNumber());
        if (isExistOrderNumber != 0)
            throw new FashionServerException(ErrorCode.valueOf("ORDER_NUMBER_DUPLICATION_ERROR").getMessage(), 631);
        orderMapper.insertOrder(orderDto);
        return orderMapper.getUserOrder(orderDto.getOrderNumber());
    }

    public List<OrderDto> getUserOrderList(int userId) throws ParseException {
        List<OrderDto> responseOrders = orderMapper.getUserOrderList(userId);
        if (responseOrders.size() == 0)
            throw new FashionServerException(ErrorCode.valueOf("ORDER_NOT_USING_ERROR").getMessage(), 632);
        for (int i = 0; i < responseOrders.size(); i++) {
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(responseOrders.get(i).getProductInfo());
            ArrayList<ProductInfoDto> productInfoDtoList = jsonArray;
            responseOrders.get(i).setProductInfo("");
            responseOrders.get(i).setProductInfoDtoList(productInfoDtoList);
        }
        return responseOrders;
    }

    @Cacheable(cacheNames = "cartList", key = "#userId")
    public List<ProductDto> putCartList(int userId, ProductDto orderProductList) {
        List<ProductDto> productDtoList = new ArrayList<>();
        for (int i = 0; i < orderProductList.getProductDtoList().size(); i++) {
            ProductDto productDto = productService.getDetailProduct(orderProductList.getProductDtoList().get(i).getId());
            productDto.setSaleQuantity(orderProductList.getProductDtoList().get(i).getSaleQuantity());
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    public List<ProductDto> getCartList(int userId) throws ParseException {
        String redisKey = "cartList::" + userId;
        String redisValue = redisTemplate.opsForValue().get(redisKey);
        if (redisValue == null) {
            log.debug("장바구니에 담긴 상품이 없습니다.");
            throw new FashionServerException(ErrorCode.valueOf("CART_PRODUCT_NOT_USING_ERROR").getMessage(), 640);
        }
        List<ProductDto> productDtoList = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonParser.parse(redisValue);
        List<JSONObject> jsonArrayProduct = (List<JSONObject>) jsonArray.get(1);
        for (int i = 0; i < jsonArrayProduct.size(); i++) {
            ProductDto productDto = productService.getDetailProduct(Integer.parseInt(String.valueOf(jsonArrayProduct.get(i).get("id"))));
            productDto.setSaleQuantity(Integer.parseInt(String.valueOf(jsonArrayProduct.get(i).get("saleQuantity"))));
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    @CacheEvict(cacheNames = "cartList", key = "#userId", beforeInvocation = true)
    public void deleteCartList(int userId) {
        log.debug("장바구니 상품 삭제");
    }
}
