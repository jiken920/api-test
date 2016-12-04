package com.test.api;

import com.google.gson.Gson;
import com.test.api.model.Product;
import com.test.api.services.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    private Product expectedProduct;

    @Before
    public void setup() {
        Set<String> tags = new HashSet<>();
        tags.add("product");
        tags.add("computer");
        expectedProduct = new Product("test", 9.99, tags, null);
    }

    @Test
    public void addProduct() throws Exception {

    }

    @Test
    public void getAllProducts() throws Exception {

        Collection<Product> products = new ArrayList<>();
        products.add(expectedProduct);
        given(this.productService.getAllProducts()).willReturn(products);

        MvcResult result = mvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$[0].name", is("test")))
                .andExpect(jsonPath("$[0].price", is(9.99)))
                .andExpect(jsonPath("$[0].code", notNullValue()))
                .andExpect(jsonPath("$[0].tags", notNullValue()))
                .andReturn();

        verify(productService, times(1)).getAllProducts();

        String content = result.getResponse().getContentAsString();
        Gson mapper = new Gson();
        Product[] actualProducts = mapper.fromJson(content, Product[].class);

        for(Product actualProduct : actualProducts) {
            assertEquals(actualProduct, expectedProduct);
        }
    }

    @Test
    public void getAllProductsThrowsException() throws Exception {
        given(this.productService.getAllProducts()).willReturn(null);

        MvcResult result = mvc.perform(get("/api/v1/products"))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(productService, times(1)).getAllProducts();
        assertThat(result.getResolvedException(), instanceOf(ProductNotFoundException.class));
    }

    @Test
    public void getProductByCode() throws Exception {

        String expectedCode = expectedProduct.getCode();
        given(this.productService.getProductByCode(expectedCode)).willReturn(expectedProduct);

        MvcResult result = mvc.perform(get("/api/v1/products/code/" + expectedCode))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name", is("test")))
                .andExpect(jsonPath("$.price", is(9.99)))
                .andExpect(jsonPath("$.code", is(expectedCode)))
                .andExpect(jsonPath("$.tags", notNullValue()))
                .andReturn();

        verify(productService, times(1)).getProductByCode(expectedCode);

        String content = result.getResponse().getContentAsString();
        Gson mapper = new Gson();
        Product actualProduct = mapper.fromJson(content, Product.class);

        assertEquals(actualProduct, expectedProduct);
    }

    @Test
    public void getProductsByTag() throws Exception {

    }

}