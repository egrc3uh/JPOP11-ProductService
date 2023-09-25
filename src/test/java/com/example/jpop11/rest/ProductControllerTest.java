package com.example.jpop11.rest;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.jpop11.dto.ProductDTO;
import com.example.jpop11.error.ProductNotFoundException;
import com.example.jpop11.repository.ProductRepository;
import com.example.jpop11.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Collections;

import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @Test
    void givenProduct_getAllProducts_success() throws Exception {
        given(this.productService.findAll()).willReturn(Collections.emptyList());
        this.mvc.perform(get("/products").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenProduct_whenGetProduct_success() throws Exception {
        ProductDTO product = new ProductDTO(1L, "Test", "test is good",4.67);
        given(this.productService.findById(1L)).willReturn(product);

            this.mvc.perform(get("/products/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenProductNotFound_failed() throws Exception {
        given(this.productService.findById(1L)).willThrow(new ProductNotFoundException("Product not found: 1"));
        this.mvc.perform(get("/products/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    void whenCreateProduct_success() throws Exception {
        ProductDTO customer = new ProductDTO(1L, "Test", "test is good",4.67);
        given(this.productService.createProduct(any(ProductDTO.class))).willReturn(customer);

        String productJson = "{\"name\": \"Test\", \"description\": \"test is good\", \"price\": \"4.67\"}";
        final MockHttpServletRequestBuilder request = post("/products")
            .content(productJson)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(header().exists("Location"))
            .andExpect(header().string("Location", "http://localhost/products/1"))
            .andExpect((ResultMatcher) jsonPath("$.name", is("Test")));
    }

    @Test
    void givenProduct_whenDelete_success() throws Exception {
        willDoNothing().given(this.productService).delete(1L);
        this.mvc.perform(delete("/products/1"))
            .andExpect(status().isNoContent());
    }
}
