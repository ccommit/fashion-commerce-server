package com.ccommit.fashionserver.mapper;

import com.ccommit.fashionserver.dto.ProductDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * packageName    : com.ccommit.fashionserver.mapper
 * fileName       : ProductMapper
 * author         : juoiy
 * date           : 2023-08-24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-24        juoiy       최초 생성
 */
@Mapper
public interface ProductMapper {

    int insertProduct(ProductDto productDto);

    int updateProduct(ProductDto productDto);

    ProductDto detailProduct(int id);

    List<ProductDto> listProduct();

    List<ProductDto> sortProduct(int categoryId, String searchType);
}
