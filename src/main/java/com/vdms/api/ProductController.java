package com.vdms.api;

import com.vdms.api.model.Product;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * Created by Kenji on 12/1/2016.
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    @Resource
    private ProductService productService;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getProductByCode(@RequestBody String code) {
        return productService.getProductByCode(code);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<Product> getProductsByTag(@RequestBody String tag) {
        return productService.getProductsByTag(tag);
    }
}
