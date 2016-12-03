package com.vdms.api;

import com.vdms.api.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Thread-safe service which allows for the insertion and retrieval of products
 * based on their unique codes or associated tags.
 */
@Service
public class ProductService {
    private static final ConcurrentMap<String, Product> products;
    private static final ConcurrentMap<String, List<Product>> productsByTag;

    static {
        products = new ConcurrentHashMap<>();
        productsByTag = new ConcurrentHashMap<>();
    }

    public Product addProduct(Product product) {

        // Associate the product with its code
        products.put(product.getCode(), product);

        // Associate the product with its tags
        for(String tag : product.getTags()) {
            if(productsByTag.containsKey(tag)) {
                List<Product> existingProducts = productsByTag.get(tag);
                existingProducts.add(product);
            } else {
                List<Product> newProducts = new ArrayList<>();
                newProducts.add(product);
                productsByTag.put(tag, newProducts);
            }
        }
        return product;
    }

    public Collection<Product> getAllProducts() {
        return products.values();
    }

    public Product getProductByCode(String code) {
        return products.get(code);
    }

    public List<Product> getProductsByTag(String tag) {
        return productsByTag.get(tag);
    }
}
