package com.ccommit.fashionserver.service;

import com.ccommit.fashionserver.dto.ProductDto;
import com.ccommit.fashionserver.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.ccommit.fashionserver.service
 * fileName       : ProductService
 * author         : juoiy
 * date           : 2023-08-24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-24        juoiy       최초 생성
 */
@Service
public class ProductService {
    @Autowired
    ProductMapper productMapper;

    public int insertProduct(ProductDto productDto) {
        int result = productMapper.insertProduct(productDto);
        return result;
    }

    public int updateProduct(ProductDto productDto) {
        int result = productMapper.updateProduct(productDto);
        return result;
    }

    //TODO: 상품리스트 조회
    //TODO:
    //
}
