package com.epam.data.handler.services;

import com.epam.data.handler.entities.Product;
import com.epam.data.handler.entities.ProductType;
import com.epam.data.handler.exceptions.EntityNotFoundException;
import com.epam.data.handler.repositories.ProductRepository;
import com.epam.data.handler.repositories.ProductTypeRepository;
import com.epam.data.handler.utils.GenericBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;


@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductTypeRepository productTypeRepository;

	private static final String COMMON_PREFIX = "EPAM generated product";
	private static final String PRODUCT_PATTERN = "^" + COMMON_PREFIX + " (.*)";
	private static final String FORMAT_PATTERN = COMMON_PREFIX + " %s";

	public List<Product> createProducts(Product product) {
		ProductType productType = getProductType(product);

		int count = 1;
		if (Objects.nonNull(product.getCount()) && product.getCount() > 0) {
			count = product.getCount();
		}
		return IntStream.rangeClosed(1, count).mapToObj(i -> {
			Product productToCreate = GenericBuilder.of(Product::new)
					.with(p -> p.setTestId(product.getTestId()))
					.with(p -> p.setCost(product.getCost()))
					.with(p -> p.setDescription(generateProductDescription(product.getTestId())))
					.with(p -> p.setProductType(productType))
					.build();
			
			Product newProduct = productRepository.save(productToCreate);
			newProduct.setTestId(product.getTestId());
			return newProduct;
		}).collect(Collectors.toList());
	}

	public Product getProductByTestId(String testId, Long productId) {
		GenericBuilder<Product> productBuilder = GenericBuilder.of(Product::new)
				.with(p -> p.setDescription(generateProductDescription(testId)));
		if (Objects.nonNull(productId) && productId > 0) {
			productBuilder.with(p -> p.setId(productId));
		}
		Product product = productBuilder.build();

		Example<Product> example = getExample(product, ExampleMatcher.StringMatcher.EXACT);
		return setTestId(productRepository.findOne(example)
								 .orElseThrow(() -> new EntityNotFoundException(product)));
	}

	public Page<Product> getProducts(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return new PageImpl<>(productRepository.findAll(pageable)
				.filter(p -> p.getDescription() != null && p.getDescription().startsWith(COMMON_PREFIX)).toList())
				.map(this::setTestId);
	}

	public void deleteProducts(String testId) {
		List<Product> productsToDelete;
		if (Objects.isNull(testId)) {
			productsToDelete = StreamSupport.stream(productRepository.findAll().spliterator(), false)
					.filter(p -> p.getDescription() != null && p.getDescription().startsWith(COMMON_PREFIX)).collect(Collectors.toList());
		}
		else {
			Product productGenericBuilder = GenericBuilder.of(Product::new)
			.with(p -> p.setDescription(generateProductDescription(testId))).build();
			productsToDelete = StreamSupport.stream(productRepository.findAll(getExample(productGenericBuilder, ExampleMatcher.StringMatcher.EXACT)).spliterator(), false)
					.collect(Collectors.toList());
		}
		productRepository.deleteAll(productsToDelete);
	}

	private ProductType getProductType(Product product) {
		GenericBuilder<ProductType> productType = GenericBuilder.of(ProductType::new);
		if (Objects.isNull(product.getProductType()) || Objects.isNull(product.getProductType().getId())) {
			final long DEFAULT_PRODUCT_TYPE_ID = 1;
			productType.with(p -> p.setId(DEFAULT_PRODUCT_TYPE_ID));
		}
		else {
			productType.with(p -> p.setId(product.getProductType().getId()));
		}
		return productTypeRepository.findOne(Example.of(productType.build()))
				.orElseGet(() -> productTypeRepository.save(productType.with(s -> s.setDescription("Default product type")).build()));
	}

	private Example<Product> getExample(Product product, ExampleMatcher.StringMatcher strMatcher) {
		ExampleMatcher matcher = ExampleMatcher.matchingAll().withStringMatcher(strMatcher);
		return Example.of(product, matcher);
	}

	public String generateProductDescription(String testId) {
		return String.format(FORMAT_PATTERN, testId);
	}

	private Product setTestId(Product product) {
		Pattern pattern = Pattern.compile(PRODUCT_PATTERN);
		Matcher testIdMatcher = pattern.matcher(product.getDescription());
		boolean isMatchFound = testIdMatcher.find();
		if (isMatchFound) {
			product.setTestId(testIdMatcher.group(1));
		}
		return product;
	}
}
