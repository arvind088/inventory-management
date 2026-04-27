package com.examples.inventory.view;

import java.util.List;

import com.examples.inventory.model.Product;

public interface ProductView {

	void showAllProducts(List<Product> products);

	void productAdded(Product product);

	void productUpdated(Product product);

	void productRemoved(Product product);

	void showError(String message, Product product);

	void showErrorProductNotFound(String message, Product product);
}
