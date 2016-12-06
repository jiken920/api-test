package com.test.api.services;

import com.test.api.model.Product;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class ProductServiceImplTest {

    private ProductService productService;
    private Product sampleProduct1;
    private Product sampleProduct2;
    private Product sampleProduct3;

    @Before
    public void setup() {
        productService = new ProductServiceImpl();

        Set<String> tags = new HashSet<>();
        tags.add("mac");
        tags.add("computer");
        tags.add("laptop");
        sampleProduct1 = new Product("macbook pro", 1999.99, tags, null);
        tags = new HashSet<>();
        tags.add("windows");
        tags.add("computer");
        sampleProduct2 = new Product("dell desktop", 850.00, tags, null);
        tags = new HashSet<>();
        tags.add("windows");
        tags.add("computer");
        tags.add("laptop");
        sampleProduct3 = new Product("surface pro", 1299.99, tags, null);
    }

    @Test
    public void addProduct() throws Exception {
        Product actualProduct = productService.addProduct(sampleProduct1);
        assertEquals(sampleProduct1, actualProduct);
        actualProduct = productService.addProduct(sampleProduct2);
        assertEquals(sampleProduct2, actualProduct);
        actualProduct = productService.addProduct(sampleProduct3);
        assertEquals(sampleProduct3, actualProduct);
    }

    @Test
    public void getAllProducts() throws Exception {
        Product expectedProduct1 = productService.addProduct(sampleProduct1);
        Product expectedProduct2 = productService.addProduct(sampleProduct2);
        Product expectedProduct3 = productService.addProduct(sampleProduct3);

        Collection<Product> actualProducts = productService.getAllProducts();
        assertEquals(3, actualProducts.size());
        assertTrue(actualProducts.contains(expectedProduct1));
        assertTrue(actualProducts.contains(expectedProduct2));
        assertTrue(actualProducts.contains(expectedProduct3));
    }

    @Test
    public void getProductByCode() throws Exception {
        Product expectedProduct1 = productService.addProduct(sampleProduct1);
        Product expectedProduct2 = productService.addProduct(sampleProduct2);
        Product expectedProduct3 = productService.addProduct(sampleProduct3);

        String expectedCode = expectedProduct2.getCode();

        Product actualProduct = productService.getProductByCode(expectedCode);

        assertEquals(expectedCode, actualProduct.getCode());
    }

    @Test
    public void getProductsByTag() throws Exception {
        Product expectedProduct1 = productService.addProduct(sampleProduct1);
        Product expectedProduct2 = productService.addProduct(sampleProduct2);
        Product expectedProduct3 = productService.addProduct(sampleProduct3);

        Collection<Product> actualProducts = productService.getProductsByTag("windows");

        assertEquals(2, actualProducts.size());
        for(Product actualProduct : actualProducts) {
            assertTrue(actualProduct.getTags().contains("windows"));
        }
    }
}