package com.ccommit.fashionserver.service;

import com.ccommit.fashionserver.dto.CategoryType;
import com.ccommit.fashionserver.dto.ProductDto;
import com.ccommit.fashionserver.dto.SearchType;
import com.ccommit.fashionserver.mapper.ProductMapper;
import com.ccommit.fashionserver.utils.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class ProductService {
    //TODO: logger 이용한 로깅처리 추
    // private static final Logger logger = LogManager.getLogger(ProductService.class);
    @Autowired
    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }


    public List<ProductDto> listProduct() {
        List<ProductDto> productDtoList = productMapper.listProduct();
        return productDtoList;
    }

    public String isResultMessage(int result) {
        if (result == ResultMessage.FAIL.getCode())
            return ResultMessage.FAIL.getMessage();
        else
            return ResultMessage.SUCCESS.getMessage();
    }

    public String insertProduct(ProductDto productDto) {
        if (productDto.getCategoryId() == CategoryType.CLOTHING.getNumber())
            productDto.setCategoryId(CategoryType.CLOTHING.getNumber());
        if (productDto.getCategoryId() == CategoryType.BAG.getNumber())
            productDto.setCategoryId(CategoryType.BAG.getNumber());
        if (productDto.getCategoryId() == CategoryType.ACCESSORY.getNumber())
            productDto.setCategoryId(CategoryType.ACCESSORY.getNumber());
        if (productDto.getCategoryId() == CategoryType.SHOES.getNumber())
            productDto.setCategoryId(CategoryType.SHOES.getNumber());
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

    public List<ProductDto> sortProduct(int categoryId, String searchType) {
        searchType = searchType.toUpperCase();
        if (searchType.equals(SearchType.NEW))
            searchType = SearchType.NEW.getName();
        if (searchType.equals(SearchType.LOW_PRICE))
            searchType = SearchType.LOW_PRICE.getName();
        if (searchType.equals(SearchType.HIGH_PRICE))
            searchType = SearchType.HIGH_PRICE.getName();
        if (searchType.equals(SearchType.LIKE))
            searchType = SearchType.LIKE.getName();
        List<ProductDto> productDtoList = productMapper.sortProduct(categoryId, searchType);
        return productDtoList;
    }

}
