package com.epam.data.handler.controller;

import com.epam.data.handler.entities.Product;
import com.epam.data.handler.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.epam.data.handler.utils.Constants.MAX_NUMBER_PAGES;
import static com.epam.data.handler.utils.Constants.MAX_NUMBER_PRODUCTS;
import static com.epam.data.handler.utils.Constants.ZERO;


@RestController
@Validated
public class ProductControllerImpl implements ProductController {

	@Autowired
	private ProductService productService;

	@Override
	public ResponseEntity<List<Product>> createProducts(Product product) {
		List<Product> result = productService.createProducts(product);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Product> getProductByTestId(String testId, Long productId) {
		Product result = productService.getProductByTestId(testId, productId);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Page<Product>> getProducts(int page, int size) {
		page = page > MAX_NUMBER_PAGES ? MAX_NUMBER_PAGES : page;
		page = page < ZERO ? ZERO : page;

		size = size > MAX_NUMBER_PRODUCTS ? MAX_NUMBER_PRODUCTS : size;
		size = size < ZERO ? ZERO : size;

		Page<Product> result = productService.getProducts(page, size);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	public ResponseEntity deleteProducts(String testId) {
		productService.deleteProducts(testId);
		return ResponseEntity.noContent().build();
	}
}
