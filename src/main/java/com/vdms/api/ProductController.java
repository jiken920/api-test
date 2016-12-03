package com.vdms.api;

import com.vdms.api.model.Product;
import com.vdms.api.services.ProductService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * Created by Kenji on 12/1/2016.
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    @Resource
    private ProductService productService;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Product addProduct(@Validated @RequestBody Product product) {
        return productService.addProduct(product);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @RequestMapping(value = "/code/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getProductByCode(@PathVariable String code) {
        return productService.getProductByCode(code);
    }

    @RequestMapping(value = "/tag/{tag}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<Product> getProductsByTag(@PathVariable String tag) {
        return productService.getProductsByTag(tag);
    }
}
