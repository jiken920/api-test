package com.test.api.services;

import com.test.api.model.Product;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
public class ProductServiceImpl implements ProductService {
    private final ConcurrentMap<String, Product> products;
    private final ConcurrentMap<String, List<Product>> productsByTag;

    public ProductServiceImpl() {
        products = new ConcurrentHashMap<>();
        productsByTag = new ConcurrentHashMap<>();
    }

    @CachePut(value = "products")
    public Product addProduct(Product product) {

        // Map the product by its unique code
        products.put(product.getCode(), product);

        // Map the product with its tags, if we have any
        if(product.getTags() != null && !product.getTags().isEmpty()) {
            for (String tag : product.getTags()) {
                // If the tag already exists, append the new product to the list of tagged products. Otherwise, create
                // a new tag-product pair.
                if (productsByTag.containsKey(tag)) {
                    List<Product> existingProducts = productsByTag.get(tag);
                    existingProducts.add(product);
                } else {
                    List<Product> newProducts = new ArrayList<>();
                    newProducts.add(product);
                    productsByTag.put(tag, newProducts);
                }
            }
        }
        return product;
    }

    @Cacheable(value = "products")
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
