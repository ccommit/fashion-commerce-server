package com.ccommit.fashionserver.mapper;

import com.ccommit.fashionserver.dto.OrderDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * packageName    : com.ccommit.fashionserver.mapper
 * fileName       : OrderMapper
 * author         : juoiy
 * date           : 2023-09-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-27        juoiy       최초 생성
 */
@Mapper
public interface OrderMapper {
    int insertOrder(OrderDto orderDto);

    List<OrderDto> getUserOrderList(int userId);

    OrderDto getUserOrder(String orderNumber);

    int updateSaleQuantity(int resultQuantity, int productId);

    int isExistOrderNumber(String orderNumber);
}
