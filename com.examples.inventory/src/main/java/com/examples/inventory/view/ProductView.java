package com.examples.inventory.view;

import com.examples.inventory.model.Product;

public interface ProductView {

	void productAdded(Product product);

	void showError(String message, Product product);
}
