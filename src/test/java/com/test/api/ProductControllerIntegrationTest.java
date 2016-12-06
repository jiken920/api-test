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
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    private Product sampleProduct1;
    private Product sampleProduct2;
    private Product sampleProduct3;

    @Before
    public void setup() {
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
        String expectedCode = sampleProduct1.getCode();
        given(this.productService.addProduct(sampleProduct1)).willReturn(sampleProduct1);

        Gson mapper = new Gson();
        String json = mapper.toJson(sampleProduct1);
        MvcResult result = mvc.perform(post("/api/v1/products/")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name", is("macbook pro")))
                .andExpect(jsonPath("$.price", is(1999.99)))
                .andExpect(jsonPath("$.code", is(expectedCode)))
                .andExpect(jsonPath("$.tags", notNullValue()))
                .andReturn();

        verify(productService, times(1)).addProduct(sampleProduct1);

        String content = result.getResponse().getContentAsString();
        Product actualProduct = mapper.fromJson(content, Product.class);

        assertEquals(actualProduct, sampleProduct1);
    }

    @Test
    public void addProductWithoutName() throws Exception {
        Set<String> tags = new HashSet<>();
        tags.add("computer");
        Product badProduct = new Product(null, 1999.99, tags, null);

        Gson mapper = new Gson();
        String json = mapper.toJson(badProduct);
        mvc.perform(post("/api/v1/products/")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addProductWithBadPrice() throws Exception {
        Set<String> tags = new HashSet<>();
        tags.add("computer");
        Product badProduct = new Product("bad product", -1.00, tags, null);

        Gson mapper = new Gson();
        String json = mapper.toJson(badProduct);
        mvc.perform(post("/api/v1/products/")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addProductWithEmptyTags() throws Exception {
        Product badProduct = new Product("bad product", 1999.99, new HashSet<>(), null);

        Gson mapper = new Gson();
        String json = mapper.toJson(badProduct);
        mvc.perform(post("/api/v1/products/")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addEmptyProductPayload() throws Exception {
        mvc.perform(post("/api/v1/products/")
                .content("")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllProducts() throws Exception {
        Collection<Product> products = new ArrayList<>();
        products.add(sampleProduct1);
        given(this.productService.getAllProducts()).willReturn(products);

        MvcResult result = mvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$[0].name", is("macbook pro")))
                .andExpect(jsonPath("$[0].price", is(1999.99)))
                .andExpect(jsonPath("$[0].code", notNullValue()))
                .andExpect(jsonPath("$[0].tags", notNullValue()))
                .andReturn();

        verify(productService, times(1)).getAllProducts();

        String content = result.getResponse().getContentAsString();
        Gson mapper = new Gson();
        Product[] actualProducts = mapper.fromJson(content, Product[].class);

        for(Product actualProduct : actualProducts) {
            assertEquals(actualProduct, sampleProduct1);
        }
    }

    @Test
    public void getAllProductsNotFound() throws Exception {
        given(this.productService.getAllProducts()).willReturn(null);

        MvcResult result = mvc.perform(get("/api/v1/products"))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(productService, times(1)).getAllProducts();
        assertThat(result.getResolvedException(), instanceOf(ProductNotFoundException.class));
    }

    @Test
    public void getProductByCode() throws Exception {
        String expectedCode = sampleProduct1.getCode();
        given(this.productService.getProductByCode(expectedCode)).willReturn(sampleProduct1);

        MvcResult result = mvc.perform(get("/api/v1/products/code/" + expectedCode))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name", is("macbook pro")))
                .andExpect(jsonPath("$.price", is(1999.99)))
                .andExpect(jsonPath("$.code", is(expectedCode)))
                .andExpect(jsonPath("$.tags", notNullValue()))
                .andReturn();

        verify(productService, times(1)).getProductByCode(expectedCode);

        String content = result.getResponse().getContentAsString();
        Gson mapper = new Gson();
        Product actualProduct = mapper.fromJson(content, Product.class);

        assertEquals(actualProduct, sampleProduct1);
    }

    @Test
    public void getProductByCodeNotFound() throws Exception {
        String expectedCode = sampleProduct1.getCode();
        given(this.productService.getProductByCode(expectedCode)).willReturn(null);

        MvcResult result = mvc.perform(get("/api/v1/products/code/" + expectedCode))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(productService, times(1)).getProductByCode(expectedCode);
        assertThat(result.getResolvedException(), instanceOf(ProductNotFoundException.class));
    }

    @Test
    public void getProductsByTag() throws Exception {
        Collection<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(sampleProduct1);
        expectedProducts.add(sampleProduct3);

        final String LAPTOP_TAG = "laptop";
        given(this.productService.getProductsByTag(LAPTOP_TAG)).willReturn(expectedProducts);

        MvcResult result = mvc.perform(get("/api/v1/products/tag/" + LAPTOP_TAG))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andReturn();

        verify(productService, times(1)).getProductsByTag(LAPTOP_TAG);

        String content = result.getResponse().getContentAsString();
        Gson mapper = new Gson();
        Product[] actualProducts = mapper.fromJson(content, Product[].class);

        for(Product actualProduct : actualProducts) {
            assertTrue(actualProduct.getTags().contains(LAPTOP_TAG));
            assertTrue(expectedProducts.contains(actualProduct));
        }
    }

    @Test
    public void getProductsByTagNotFound() throws Exception {
        final String tag = "computer";
        given(this.productService.getProductsByTag(tag)).willReturn(null);

        MvcResult result = mvc.perform(get("/api/v1/products/tag/" + tag))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(productService, times(1)).getProductsByTag(tag);
        assertThat(result.getResolvedException(), instanceOf(ProductNotFoundException.class));
    }
}