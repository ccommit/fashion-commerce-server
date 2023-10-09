package com.ccommit.fashionserver.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.sql.Date;

/**
 * packageName    : com.ccommit.fashionserver.dto
 * fileName       : ProductDto
 * author         : juoiy
 * date           : 2023-08-24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-24        juoiy       최초 생성
 */
@Getter
@Setter
@Builder
public class ProductInfoDto {
    private int id;                 // 상품번호
    @NotBlank
    private String name;            // 상품명
    @NotBlank
    private String saleQuantity;    // 판매수량
    @NotBlank
    private String price;           // 가격
}
