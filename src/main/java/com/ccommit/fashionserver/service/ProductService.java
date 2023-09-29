package com.ccommit.fashionserver.service;

import com.ccommit.fashionserver.dto.CategoryType;
import com.ccommit.fashionserver.dto.ProductDto;
import com.ccommit.fashionserver.dto.SearchType;
import com.ccommit.fashionserver.mapper.ProductMapper;
import com.ccommit.fashionserver.utils.ResultMessage;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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
@Log4j2
@Service
public class ProductService {
    @Autowired
    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public List<ProductDto> getProductList(String categoryName, String searchType) {
        int categoryId = 0;
        for (CategoryType categoryType : CategoryType.values()) {
            if (categoryName.equals(categoryType.getName())) {
                categoryId = categoryType.getNumber();
                break;
            }
        }
        if (categoryId == 0)
            throw new IllegalArgumentException("존재하지 않는 카테고리입니다.");

        searchType = searchType.toUpperCase();
        for (SearchType search : SearchType.values()) {
            if (searchType.equals(search.getName())) {
                searchType = search.getName();
                break;
            }
        }
        log.info("categoryId = " + categoryId + ", searchType = " + searchType);
        List<ProductDto> productDtoList = productMapper.getProductList(categoryId, searchType);
        return productDtoList;
    }

    public String isResultMessage(int result) {
        if (result == ResultMessage.FAIL.getCode())
            return ResultMessage.FAIL.getMessage();
        else
            return ResultMessage.SUCCESS.getMessage();
    }

    public String insertProduct(ProductDto productDto) {
        Arrays.stream(CategoryType.values())
                .filter(categoryType -> productDto.getCategoryId() == categoryType.getNumber())
                .forEach(categoryType -> {
                    if (productDto.getCategoryId() == categoryType.getNumber()) {
                        productDto.setCategoryId(categoryType.getNumber());
                    } else {
                        throw new IllegalArgumentException("존재하지 않는 카테고리입니다.");
                    }
                });
        int result = productMapper.insertProduct(productDto);
        String resultMessage = isResultMessage(result);
        return resultMessage;
    }

    public String updateProduct(ProductDto productDto) {
        int result = productMapper.updateProduct(productDto);
        String resultMessage = isResultMessage(result);
        return resultMessage;
    }

    public ProductDto detailProduct(int id) {
        ProductDto productDto = productMapper.detailProduct(id);
        return productDto;
    }

}
