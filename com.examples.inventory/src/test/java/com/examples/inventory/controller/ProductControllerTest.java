package com.examples.inventory.controller;

import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.examples.inventory.model.Product;
import com.examples.inventory.repository.ProductRepository;
import com.examples.inventory.view.ProductView;

public class ProductControllerTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private ProductView productView;

	@InjectMocks
	private ProductController productController;

	private AutoCloseable closeable;

	@Before
	public void setup() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	@Test
	public void testAllProducts() {
		List<Product> products = asList(new Product("1", "Laptop", 10, 999.99));
		when(productRepository.findAll())
			.thenReturn(products);
		productController.allProducts();
		verify(productView)
			.showAllProducts(products);
	}

	@Test
	public void testNewProductWhenProductDoesNotAlreadyExist() {
		Product product = new Product("1", "Laptop", 10, 999.99);
		when(productRepository.findById("1")).
			thenReturn(null);
		productController.newProduct(product);
		InOrder inOrder = inOrder(productRepository, productView);
		inOrder.verify(productRepository).save(product);
		inOrder.verify(productView).productAdded(product);
	}

	@Test
	public void testNewProductWhenProductAlreadyExists() {
		Product productToAdd = new Product("1", "Laptop", 10, 999.99);
		Product existingProduct = new Product("1", "Mouse", 20, 19.99);
		when(productRepository.findById("1")).
			thenReturn(existingProduct);
		productController.newProduct(productToAdd);
		verify(productView)
			.showError("Already existing product with id 1", existingProduct);
		verifyNoMoreInteractions(ignoreStubs(productRepository));
	}

	@Test
	public void testDeleteProductWhenProductExists() {
		Product productToDelete = new Product("1", "Laptop", 10, 999.99);
		when(productRepository.findById("1")).
			thenReturn(productToDelete);
		productController.deleteProduct(productToDelete);
		InOrder inOrder = inOrder(productRepository, productView);
		inOrder.verify(productRepository).delete("1");
		inOrder.verify(productView).productRemoved(productToDelete);
	}

	@Test
	public void testDeleteProductWhenProductDoesNotExist() {
		Product product = new Product("1", "Laptop", 10, 999.99);
		when(productRepository.findById("1")).
			thenReturn(null);
		productController.deleteProduct(product);
		verify(productView)
			.showErrorProductNotFound("No existing product with id 1", product);
		verifyNoMoreInteractions(ignoreStubs(productRepository));
	}

	@Test
	public void testUpdateProductWhenProductExists() {
		Product productToUpdate = new Product("1", "Laptop", 15, 899.99);
		Product existingProduct = new Product("1", "Laptop", 10, 999.99);
		when(productRepository.findById("1")).
			thenReturn(existingProduct);
		productController.updateProduct(productToUpdate);
		InOrder inOrder = inOrder(productRepository, productView);
		inOrder.verify(productRepository).update(productToUpdate);
		inOrder.verify(productView).productUpdated(productToUpdate);
	}

	@Test
	public void testUpdateProductWhenProductDoesNotExist() {
		Product product = new Product("1", "Laptop", 15, 899.99);
		when(productRepository.findById("1")).
			thenReturn(null);
		productController.updateProduct(product);
		verify(productView)
			.showErrorProductNotFound("No existing product with id 1", product);
		verifyNoMoreInteractions(ignoreStubs(productRepository));
	}
}
