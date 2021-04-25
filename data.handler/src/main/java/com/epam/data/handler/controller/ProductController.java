package com.epam.data.handler.controller;

import com.epam.data.handler.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;


public interface ProductController {

	@PostMapping(value = "/products", consumes = MediaType.ALL_VALUE, headers = "Accept=" + MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<List<Product>> createProducts(@Valid @RequestBody Product product);

	@GetMapping(value = "/products", consumes = MediaType.ALL_VALUE, headers = "Accept=" + MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Page<Product>> getProducts(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size);

	@DeleteMapping(value = "/products", consumes = MediaType.ALL_VALUE, headers = "Accept=" + MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity deleteProducts(@RequestParam(value = "testId", required = false) String testId);

	@GetMapping(value = "/product/{testId}", consumes = MediaType.ALL_VALUE,
				headers = "Accept=" + MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Product> getProductByTestId(@PathVariable("testId") String testId,
			@RequestParam(value = "productId", defaultValue = "0", required = false) Long productId);

}
