package com.ccommit.fashionserver.controller;

import com.ccommit.fashionserver.dto.ProductDto;
import com.ccommit.fashionserver.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/list")
    public ProductDto productList() {
        ProductDto productDto = new ProductDto();
        return productDto;
    }

    @PostMapping("/insert")
    public int insertProduct(ProductDto productDto) {
        int result = 0;
        return result = productService.insertProduct(productDto);
    }

    @PatchMapping("")
    public int updateProduct(ProductDto productDto) {
        int result = productService.updateProduct(productDto);
        return result;
    }

}
