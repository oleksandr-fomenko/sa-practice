package com.epam.data.handler.integration;

import com.epam.data.handler.controller.ProductController;
import com.epam.data.handler.core.AbstractApiTest;
import com.epam.data.handler.core.DataFactory;
import com.epam.data.handler.entities.Product;
import com.epam.data.handler.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class ProductControllerIntegrationTests extends AbstractApiTest {

    @MockBean
    private ProductService productService;
    @Autowired
    @Qualifier("productControllerFeign")
    private ProductController productControllerFeign;

    @BeforeEach
    void setUp() {
        Mockito.reset(productService);
    }

    @Test
    void whenSendIncorrectAuthRequestOnGetProducts_thenUnauthorizedStatusCodeReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(login, password + 1))
                .contentType(mediaTypeJson))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void whenSendValidRequestOnGetProductByTestId_thenProductIsReturned() throws Exception {
        String testId = "INT_API_TEST_1";
        Product product = DataFactory.getProductOne(productService.generateProductDescription(testId));
        product.setTestId(testId);
        Long productId = product.getId();
        Mockito.when(productService.getProductByTestId(testId, productId)).thenReturn(product);
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(product);

        mockMvc.perform(MockMvcRequestBuilders.get("/product/{testId}", testId)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(login, password))
                .param("productId", productId.toString())
                .contentType(mediaTypeJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedJson))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(mediaTypeJson));
    }

    @Test
    void whenSendValidRequestOnGetProductByTestId_thenProductIsReturnedFeign(){
        String testId = "INT_API_TEST_2";
        Product product = DataFactory.getProductOne(productService.generateProductDescription(testId));
        product.setTestId(testId);
        Long productId = product.getId();
        Mockito.when(productService.getProductByTestId(testId, productId)).thenReturn(product);

        ResponseEntity<Product> productsResponse = productControllerFeign.getProductByTestId(testId,productId);
        Assertions.assertEquals(HttpStatus.OK, productsResponse.getStatusCode());
        Assertions.assertNotNull(productsResponse.getBody());
        Assertions.assertEquals(product.getId(), productsResponse.getBody().getId(), "Verify that product has correct productId");
        Assertions.assertEquals(product.getTestId(), productsResponse.getBody().getTestId(), "Verify that product has correct testId");
    }

}
