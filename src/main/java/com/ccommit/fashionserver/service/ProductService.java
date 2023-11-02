package com.ccommit.fashionserver.service;

import com.ccommit.fashionserver.dto.CategoryType;
import com.ccommit.fashionserver.dto.ProductDto;
import com.ccommit.fashionserver.dto.SearchType;
import com.ccommit.fashionserver.exception.ErrorCode;
import com.ccommit.fashionserver.exception.FashionServerException;
import com.ccommit.fashionserver.mapper.ProductMapper;
import lombok.extern.log4j.Log4j2;
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
        String searchTypeTemp = "";
        if (categoryName == null)
            categoryName = "전체";
        if (searchType.equals("") || searchType == null)
            throw new FashionServerException(ErrorCode.valueOf("SEARCH_TYPE_NOT_USING_ERROR").getMessage(), 621);
        int categoryAllNumber = CategoryType.ALL.getNumber();
        for (CategoryType categoryType : CategoryType.values()) {
            if (categoryName.equals(categoryType.getName())) {
                categoryId = categoryType.getNumber();
                break;
            }
        }
        if (categoryId == 0) {
            log.debug("존재하지 않는 카테고리입니다.");
            throw new FashionServerException(ErrorCode.valueOf("CATEGORY_NOT_USING_ERROR").getMessage(), 605);
        }
        searchType = searchType.toUpperCase();
        for (SearchType search : SearchType.values()) {
            if (searchType.equals(search.getName())) {
                searchTypeTemp = search.getName();
                break;
            }
        }
        List<ProductDto> productDtoList = productMapper.getProductList(categoryId, searchTypeTemp, categoryAllNumber);
        if (productDtoList == null)
            throw new FashionServerException(ErrorCode.valueOf("PRODUCT_NOT_USING_ERROR").getMessage(), 613);
        return productDtoList;
    }

    public ProductDto getDetailProduct(int productId) {
        ProductDto productDto = productMapper.getDetailProduct(productId);
        if (productDto == null)
            throw new FashionServerException(ErrorCode.valueOf("PRODUCT_NOT_USING_ERROR").getMessage(), 613);
        return productDto;
    }

    public ProductDto insertProduct(Integer loginSession, ProductDto productDto) {
        Arrays.stream(CategoryType.values())
                .filter(categoryType -> productDto.getCategoryId() == categoryType.getNumber())
                .forEach(categoryType -> {
                    if (productDto.getCategoryId() == categoryType.getNumber()) {
                        productDto.setCategoryId(categoryType.getNumber());
                    } else {
                        log.debug("존재하지 않는 카테고리입니다.");
                        throw new FashionServerException(ErrorCode.valueOf("CATEGORY_NOT_USING_ERROR").getMessage(), 605);
                    }
                });
        productDto.setSaleId(loginSession);
        int result = productMapper.insertProduct(productDto);
        if (result == 0)
            throw new FashionServerException(ErrorCode.valueOf("PRODUCT_INSERT_ERROR").getMessage(), 610);
        return productMapper.getDetailProduct(productDto.getId());
    }

    public ProductDto updateProduct(Integer loginSession, ProductDto productDto) {
        productDto.setSaleId(loginSession);
        if (productMapper.getDetailProduct(productDto.getId()) == null) {
            log.debug("존재하지 않는 상품입니다.");
            throw new FashionServerException(ErrorCode.valueOf("PRODUCT_NOT_USING_ERROR").getMessage(), 613);
        }
        int result = productMapper.updateProduct(productDto);
        if (result == 0)
            throw new FashionServerException(ErrorCode.valueOf("PRODUCT_UPDATE_ERROR").getMessage(), 611);
        return productMapper.getDetailProduct(productDto.getId());
    }

    public void deleteProduct(int id) {
        if (productMapper.getDetailProduct(id) == null) {
            log.debug("존재하지 않는 상품입니다.");
            throw new FashionServerException(ErrorCode.valueOf("PRODUCT_NOT_USING_ERROR").getMessage(), 613);
        }
        int result = productMapper.deleteProduct(id);
        if (result == 0) {
            log.debug("상품 삭제에 실패하였습니다.");
            throw new FashionServerException(ErrorCode.valueOf("PRODUCT_DELETE_ERROR").getMessage(), 612);
        }
    }
}
