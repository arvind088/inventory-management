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

	public void deleteProduct(Product product) {
		if (productRepository.findById(product.getId()) == null) {
			productView.showErrorProductNotFound("No existing product with id " + product.getId(),
				product);
			return;
		}

		productRepository.delete(product.getId());
		productView.productRemoved(product);
	}
}
