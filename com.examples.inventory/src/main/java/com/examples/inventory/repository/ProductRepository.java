package com.examples.inventory.repository;

import java.util.List;

import com.examples.inventory.model.Product;

public interface ProductRepository {

	void save(Product product);

	List<Product> findAll();

	Product findById(String id);

	void update(Product product);

	void delete(String id);
}
