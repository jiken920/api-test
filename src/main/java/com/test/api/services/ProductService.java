package com.test.api.services;

import com.test.api.model.Product;

import java.util.Collection;

/**
 * Created by Kenji on 12/3/2016.
 */
public interface ProductService {

    Product addProduct(Product product);

    Collection<Product> getAllProducts();

    Product getProductByCode(String code);

    Collection<Product> getProductsByTag(String tag);
}
