package com.examples.inventory.controller;

import com.examples.inventory.model.Product;
import com.examples.inventory.repository.ProductRepository;
import com.examples.inventory.view.ProductView;

public class ProductController {

	private ProductView productView;
	private ProductRepository productRepository;

	public ProductController(ProductView productView, ProductRepository productRepository) {
		this.productView = productView;
		this.productRepository = productRepository;
	}

	public void allProducts() {
		productView.showAllProducts(productRepository.findAll());
	}

	public void newProduct(Product product) {
		Product existingProduct = productRepository.findById(product.getId());
		if (existingProduct != null) {
			productView.showError("Already existing product with id " + product.getId(),
				existingProduct);
			return;
		}

		productRepository.save(product);
		productView.productAdded(product);
	}
}
