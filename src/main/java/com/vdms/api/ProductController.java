package com.vdms.api;

import com.vdms.api.model.Product;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by Kenji on 12/1/2016.
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    @Resource
    private ProductService productService;

    @RequestMapping(method = RequestMethod.POST)
    public Product addProduct(Product product) {
        return null;
    }
}
