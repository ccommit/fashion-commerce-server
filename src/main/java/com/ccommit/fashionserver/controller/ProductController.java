package com.ccommit.fashionserver.controller;

import com.ccommit.fashionserver.dto.ProductDto;
import com.ccommit.fashionserver.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * packageName    : com.ccommit.fashionserver.controller
 * fileName       : ProductController
 * author         : juoiy
 * date           : 2023-08-24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-24        juoiy       최초 생성
 */
@Log4j2
@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public List<ProductDto> getProductList(String categoryName, String searchType) {
        List<ProductDto> productDtoList = (List<ProductDto>) productService.getProductList(categoryName, searchType);
        return productDtoList;
    }

    @PostMapping("/detail")
    public ProductDto getProductDetail(int id) {
        ProductDto productDto = productService.detailProduct(id);
        return productDto;
    }

    @PostMapping("")
    //@LoginCheck(types = LoginCheck.UserType.SELLER)
    public String insertProduct(@Valid ProductDto productDto) {
        String result = productService.insertProduct(productDto);
        return result;
    }

    @PatchMapping("")
    //@LoginCheck(types = LoginCheck.UserType.SELLER)
    public String updateProduct(ProductDto productDto) {
        String result = productService.updateProduct(productDto);
        return result;
    }


}
