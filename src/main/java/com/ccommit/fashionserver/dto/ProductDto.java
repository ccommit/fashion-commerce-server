package com.ccommit.fashionserver.dto;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

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
public class ProductDto extends JSONObject {
    private int id;                 // 상품번호
    @NotBlank
    private String name;            // 상품명
    @NotBlank
    private String saleQuantity;    // 판매수량
    @NotBlank
    private String price;           // 가격
    private int categoryId;         // 카테고리번호
    private int likeCount;          // 좋아요수
    @NotBlank
    private String brandName;       // 브랜드명
    private int saleId;             // 판매자아이디
    private Date createDate;        // 등록일
    private Date updateDate;        // 수정일
}
