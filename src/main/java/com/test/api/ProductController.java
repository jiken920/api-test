package com.test.api;

import com.test.api.model.Product;
import com.test.api.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * Created by Kenji on 12/1/2016.
 */
@RestController
@RequestMapping("/api")
public class ProductController {

    @Resource
    private ProductService productService;

    private static final String NO_PRODUCTS_MSG = "No products were found.";
    private static final String NO_PRODUCTS_WITH_TAG_MSG = "Could not find any products tagged with: ";
    private static final String NO_PRODUCT_WITH_CODE_MSG = "Could not find a product with code: ";

    @RequestMapping(value = "/v1/products",
                    method = RequestMethod.POST,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public Product addProduct(@Validated @RequestBody Product product) {
        return productService.addProduct(product);
    }

    @RequestMapping(value = "/v1/products",
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<Product> getAllProducts() {
        Collection<Product> products = productService.getAllProducts();
        if(products == null || products.isEmpty()) {
            throw new ProductNotFoundException(NO_PRODUCTS_MSG);
        } else {
            return products;
        }
    }

    @RequestMapping(value = "/v1/products/code/{code}",
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getProductByCode(@PathVariable String code) {
        Product product = productService.getProductByCode(code);
        if(product == null) {
            throw new ProductNotFoundException(NO_PRODUCT_WITH_CODE_MSG + code);
        } else {
            return product;
        }
    }

    @RequestMapping(value = "/v1/products/tag/{tag}",
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<Product> getProductsByTag(@PathVariable String tag) {
        Collection<Product> products = productService.getProductsByTag(tag);
        if(products == null || products.isEmpty()) {
            throw new ProductNotFoundException(NO_PRODUCTS_WITH_TAG_MSG + tag);
        } else {
            return products;
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    private class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }
}
