package com.ccommit.fashionserver.service;

import com.ccommit.fashionserver.dto.OrderDto;
import com.ccommit.fashionserver.dto.ProductDto;
import com.ccommit.fashionserver.dto.ProductInfoDto;
import com.ccommit.fashionserver.mapper.OrderMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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

    public OrderService(OrderMapper orderMapper, ProductService productService) {
        this.orderMapper = orderMapper;
        this.productService = productService;
    }

    public OrderDto insertOrder(int userId, List<ProductDto> orderProductList) throws JsonProcessingException {
        DecimalFormat priceFormat = new DecimalFormat("###,###");
        OrderDto orderDto = new OrderDto();
        ProductDto productDto = new ProductDto();
        int orderTotalPrice = 0;
        int index = 0;

        ArrayList<ProductInfoDto> productInfoDtoList = new ArrayList<>();
        for (int i = 0; i < orderProductList.size(); i++) {

            // 이부분에 로직있었음 따로 빼놓음. 추후 수정 필요
            int productId = orderProductList.get(i).getId();
            String saleQuantity = orderProductList.get(i).getSaleQuantity();
            productDto = productService.detailProduct(productId);
            int productQuantity = Integer.parseInt(productDto.getSaleQuantity());

            if (productDto == null) {
                throw new RuntimeException("상품이 존재하지 않습니다.");
            } else {
                log.info(productId + " 상품이 존재합니다.");
                if (productQuantity == 0)
                    throw new RuntimeException("상품의 수량이 0개입니다.");

                int orderQuantity = Integer.parseInt(saleQuantity);
                int orderPrice = orderQuantity * Integer.parseInt(productDto.getPrice().replace(",", ""));
                orderTotalPrice += orderPrice;
                log.info("productId: " + productId + ", saleQuantity: " + saleQuantity + ", orderPrice: "
                        + orderPrice + ", orderTotalPrice: " + orderTotalPrice);

                ProductInfoDto productInfoDto = ProductInfoDto.builder()
                        .id(orderProductList.get(i).getId())
                        .saleQuantity(orderProductList.get(i).getSaleQuantity())
                        .name(orderProductList.get(i).getName())
                        .price(orderProductList.get(i).getPrice())
                        .build();
                productInfoDtoList.add(productInfoDto);

                // TODO: 주문수량만큼 상품테이블에서 상품수량 차감

                index++;
            }
        }
        orderDto.setTotalPrice(((priceFormat.format(orderTotalPrice))));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(productInfoDtoList);
        orderDto.setProductInfo(json);
        orderDto.setUserId(userId);
        orderMapper.insertOrder(orderDto);
        int resultOrderId = orderDto.getId();
        return orderMapper.getUserOrder(resultOrderId);
    }


}
