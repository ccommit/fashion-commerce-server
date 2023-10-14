package com.ccommit.fashionserver.service;

import com.ccommit.fashionserver.dto.OrderDto;
import com.ccommit.fashionserver.dto.ProductDto;
import com.ccommit.fashionserver.dto.ProductInfoDto;
import com.ccommit.fashionserver.dto.ResponseOrder;
import com.ccommit.fashionserver.mapper.OrderMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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

            int productId = orderProductList.get(i).getId();
            String saleQuantity = orderProductList.get(i).getSaleQuantity();
            productDto = productService.detailProduct(productId);
            int productQuantity = Integer.parseInt(productDto.getSaleQuantity());

            if (productDto == null) {
                throw new RuntimeException("상품이 존재하지 않습니다.");
            } else {
                int orderQuantity = Integer.parseInt(saleQuantity); // 주문수량
                if (productQuantity < 1 || productQuantity < orderQuantity)
                    throw new RuntimeException("상품의 수량이 부족합니다. 확인해주세요.");

                // 여기서부터 주문가능 로직
                // 상품수량에서 주문수량 차감
                int resultQuantity = productQuantity - orderQuantity;
                //productQuantity -= orderQuantity; // 상품수량 - 주문수량
                // 차감수량만큼 상품수량 업데이트
                log.info("주문수량: " + orderQuantity + " - 상품수량: " + productQuantity + " = 차감결과수량: " + resultQuantity);
                int updateResult = orderMapper.updateSaleQuantity(resultQuantity, productId);
                if (updateResult == 0)
                    throw new RuntimeException("상품수량 업데이트에 실패하였습니다. 다시 시도해주세요.");

                int orderPrice = orderQuantity * Integer.parseInt(productDto.getPrice().replace(",", ""));
                orderTotalPrice += orderPrice;
                log.info("productId: " + productId + ", saleQuantity: " + saleQuantity + ",price: " + Integer.parseInt(productDto.getPrice().replace(",", ""))
                        + ", orderPrice: " + orderPrice + ", orderTotalPrice: " + orderTotalPrice);

                ProductInfoDto productInfoDto = ProductInfoDto.builder()
                        .id(orderProductList.get(i).getId())
                        .saleQuantity(orderProductList.get(i).getSaleQuantity())
                        .name(orderProductList.get(i).getName())
                        .price(orderProductList.get(i).getPrice())
                        .build();
                productInfoDtoList.add(productInfoDto);
                index++;
            }
        }
        orderDto.setTotalPrice(((priceFormat.format(orderTotalPrice))));
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(productInfoDtoList);
        orderDto.setProductInfo(json);
        orderDto.setUserId(userId);
        //orderMapper.insertOrder(orderDto);
        int resultOrderId = orderDto.getId();
        return orderMapper.getUserOrder(resultOrderId);
    }

    //@Cacheable(value = "order", key = "#orderId")
    public List<ResponseOrder> getUserOrderList(int userId) throws ParseException, JsonProcessingException {
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


}
