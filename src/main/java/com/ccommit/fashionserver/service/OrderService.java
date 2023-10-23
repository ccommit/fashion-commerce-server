package com.ccommit.fashionserver.service;

import com.ccommit.fashionserver.dto.OrderDto;
import com.ccommit.fashionserver.dto.ProductDto;
import com.ccommit.fashionserver.dto.ProductInfoDto;
import com.ccommit.fashionserver.dto.ResponseOrder;
import com.ccommit.fashionserver.exception.FashionServerException;
import com.ccommit.fashionserver.mapper.OrderMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
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
    private StringRedisTemplate redisTemplate;

    public OrderService(OrderMapper orderMapper, ProductService productService) {
        this.orderMapper = orderMapper;
        this.productService = productService;
    }

    public OrderDto insertOrder(int userId, List<ProductDto> orderProductList, String orderType) throws JsonProcessingException, ParseException {
        DecimalFormat priceFormat = new DecimalFormat("###,###");
        OrderDto orderDto = new OrderDto();
        ObjectMapper objectMapper = new ObjectMapper();
        int orderTotalPrice = 0;

        ArrayList<ProductInfoDto> productInfoDtoList = new ArrayList<>();
        for (int i = 0; i < orderProductList.size(); i++) {
            int orderQuantity = 0;
            int productId = (int) orderProductList.get(i).get("id");
            if (orderType.equals("CART")) {
                String cartSaleQuantity = (String) orderProductList.get(i).get("sale_quantity");
                orderQuantity = Integer.parseInt(cartSaleQuantity);
            } else {
                orderQuantity = Integer.parseInt((String) orderProductList.get(i).get("saleQuantity")); // 주문수량
            }

            ProductDto productDto = productService.getDetailProduct(productId);
            String productName = (String) productDto.get("name");
            int productQuantity = Integer.parseInt((String) productDto.get("sale_quantity"));
            String productPrice = (String) productDto.get("price");
            productPrice = productPrice.replace(",", "");
            if (productQuantity < 1 || productQuantity < orderQuantity)
                throw new FashionServerException("PRODUCT_QUANTITY_NOT_ENOUGH_ERROR", 614);

            int resultQuantity = productQuantity - orderQuantity;
            log.info("상품수량 - 주문수량 = 차감결과수량 : " + productQuantity + " - " + orderQuantity + " = " + resultQuantity);

            int updateResult = orderMapper.updateSaleQuantity(resultQuantity, productId);
            if (updateResult == 0)
                throw new FashionServerException("PRODUCT_UPDATE_ERROR", 611);

            int orderPrice = orderQuantity * Integer.parseInt(productPrice);
            orderTotalPrice += orderPrice;
            log.info("productId: " + productId + ", orderQuantity: " + orderQuantity + ",price: " + Integer.parseInt(productPrice)
                    + ", orderPrice: " + orderPrice + ", orderTotalPrice: " + orderTotalPrice);
            ProductInfoDto productInfoDto = ProductInfoDto.builder()
                    .id(productId)
                    .saleQuantity(String.valueOf(orderQuantity))
                    .name(productName)
                    .price(productPrice)
                    .build();
            productInfoDtoList.add(productInfoDto);
        }
        orderDto.setTotalPrice(((priceFormat.format(orderTotalPrice))));
        String json = objectMapper.writeValueAsString(productInfoDtoList);
        orderDto.setProductInfo(json);
        orderDto.setUserId(userId);
        orderMapper.insertOrder(orderDto);
        int resultOrderId = orderDto.getId();
        return orderMapper.getUserOrder(resultOrderId);
    }

    public List<ResponseOrder> getUserOrderList(int userId) throws ParseException {
        List<ResponseOrder> responseOrders = orderMapper.getUserOrderList(userId);
        for (int i = 0; i < responseOrders.size(); i++) {
            JSONParser jsonParser = new JSONParser();
            String productInfo = responseOrders.get(i).getProductInfo();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(productInfo);
            ArrayList<ProductInfoDto> productInfoDtoList = jsonArray;
            responseOrders.get(i).setProductInfo("");
            responseOrders.get(i).setProductInfoDtoList(productInfoDtoList);
        }
        return responseOrders;
    }

    @Cacheable(cacheNames = "cartList", key = "#userId + #orderProductList.hashCode()", unless = "#result == null")
    public List<ProductDto> putCartList(int userId, List<ProductDto> orderProductList) {
        List<ProductDto> productDtoList = new ArrayList<>();
        for (int i = 0; i < orderProductList.size(); i++) {
            ProductDto productDto = productService.getDetailProduct((Integer) orderProductList.get(i).get("id"));
            productDto.put("sale_quantity", orderProductList.get(i).get("saleQuantity"));
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    public List<ProductDto> getCartList(int userId, List<ProductDto> orderProductList) throws ParseException {
        String redisKey = "cartList::" + (userId + orderProductList.hashCode());
        String redisValue = redisTemplate.opsForValue().get(redisKey);
        if (redisValue == null) {
            log.debug("장바구니에 담긴 상품이 없습니다.");
            throw new FashionServerException("CART_PRODUCT_NOT_USING_ERROR", 640);
        }
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonParser.parse(redisValue);
        List<ProductDto> productDtoList = new ArrayList<>();
        List<ProductDto> jsonArrayProduct = (List<ProductDto>) jsonArray.get(1);
        for (int i = 0; i < jsonArrayProduct.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArrayProduct.get(i);
            int productId = Integer.parseInt(String.valueOf(jsonObject.get("id")));
            ProductDto productDto = productService.getDetailProduct(productId);
            productDto.put("sale_quantity", orderProductList.get(i).get("saleQuantity"));
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    @CacheEvict(cacheNames = "cartList", key = "#userId + #orderProductList.hashCode()")
    public void deleteCartList(int userId, List<ProductDto> orderProductList) {
        log.info("장바구니 상품 삭제");
    }
}
