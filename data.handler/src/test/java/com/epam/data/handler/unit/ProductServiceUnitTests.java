package com.epam.data.handler.unit;

import com.epam.data.handler.core.AbstractUnitTest;
import com.epam.data.handler.core.DataFactory;
import com.epam.data.handler.entities.Product;
import com.epam.data.handler.repositories.ProductRepository;
import com.epam.data.handler.repositories.ProductTypeRepository;
import com.epam.data.handler.services.ProductService;
import com.epam.data.handler.utils.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductServiceUnitTests extends AbstractUnitTest {

    @Autowired
    private ProductService productService;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private ProductTypeRepository productTypeRepository;

    @BeforeEach
    void setUp() {
        Mockito.reset(productRepository);
        Mockito.reset(productTypeRepository);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {-1, 0, 1})
    void whenNegativeOrNullableCountIsProvided_thenOneProductIsCreated(Integer count) {
        final int EXPECTED_COUNT_OF_THE_PRODUCTS = 1;
        Product expectedProduct = DataFactory.getProductOne(productService.generateProductDescription("Unit1"));
        expectedProduct.setCount(count);

        Mockito.when(productTypeRepository.findOne(ArgumentMatchers.any()))
                .thenReturn(Optional.of(expectedProduct.getProductType()));
        Mockito.when(productRepository.save(Mockito.any(Product.class)))
                .thenReturn(expectedProduct);

        List<Product> expectedProducts = productService.createProducts(expectedProduct);
        Assertions.assertEquals(EXPECTED_COUNT_OF_THE_PRODUCTS, expectedProducts.size());
    }

    @Test
    void whenCountSpecifiedMoreThatOneProductCount_thenExactCountOfTheProductsReturned() {
        final int EXPECTED_COUNT_OF_THE_PRODUCTS = 5;
        Product expectedProduct = DataFactory.getProductOne(productService.generateProductDescription("Unit1"));
        expectedProduct.setCount(EXPECTED_COUNT_OF_THE_PRODUCTS);

        Mockito.when(productTypeRepository.findOne(ArgumentMatchers.any()))
                .thenReturn(Optional.of(expectedProduct.getProductType()));
        Mockito.when(productRepository.save(Mockito.any(Product.class)))
                .thenReturn(expectedProduct);

        List<Product> expectedProducts = productService.createProducts(expectedProduct);
        Assertions.assertEquals(EXPECTED_COUNT_OF_THE_PRODUCTS, expectedProducts.size());
    }

    @Test
    void whenProductsReturnedFromRepository_thenMappedProductsAreReturned() {
        int page = Constants.ZERO;
        int size = Constants.MAX_NUMBER_PRODUCTS;

        List<Product> productsToReturn = new ArrayList<>();
        Product expectedProductOne = DataFactory.getProductOne(productService.generateProductDescription("Unit1"));
        Product expectedProductTwo = DataFactory.getProductTwo(productService.generateProductDescription("Unit2"));
        productsToReturn.add(expectedProductOne);
        productsToReturn.add(expectedProductTwo);

        Mockito.when(productRepository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(productsToReturn));

        Page<Product> actualProducts = productService.getProducts(page, size);
        Assertions.assertEquals(productsToReturn.size(), actualProducts.getTotalElements());

        Map<Long, Product> mappedProducts = productsToReturn.stream().collect(Collectors.toMap(Product::getId, v -> v));
        actualProducts.forEach(actualProduct -> {
            Assertions.assertNotNull(actualProduct.getId(), "Verify that productId is not null");
            Product expectedProduct = mappedProducts.get(actualProduct.getId());
            Assertions.assertNotNull(expectedProduct.getId(), "Verify that product exists in the product map");
            Assertions.assertEquals(expectedProduct.getTestId(), actualProduct.getTestId(), String.format("Verify that product %s has correct testId", expectedProduct.getId()));
        });
    }


}
