package com.examples.inventory.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.examples.inventory.controller.ProductController;
import com.examples.inventory.model.Product;

public class ProductSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;
	private ProductSwingView view;
	private ProductController productController;

	@Override
	protected void onSetUp() {
		productController = mock(ProductController.class);
		view = GuiActionRunner.execute(new GuiQuery<ProductSwingView>() {
			@Override
			protected ProductSwingView executeInEDT() {
				ProductSwingView productSwingView = new ProductSwingView();
				productSwingView.setProductController(productController);
				return productSwingView;
			}
		});
		window = new FrameFixture(robot(), view);
		window.show();
	}

	@Test
	public void testInitialControls() {
		window.table("productTable");
		window.textBox("idField");
		window.textBox("nameField");
		window.textBox("quantityField");
		window.textBox("priceField");
		window.button("addButton").requireText("Add");
		window.button("updateButton").requireText("Update");
		window.button("deleteButton").requireText("Delete");
		window.button("refreshButton").requireText("Refresh");
	}

	@Test
	public void testAddButtonIsEnabledOnlyWhenAllFieldsArePopulated() {
		window.button("addButton").requireDisabled();
		window.textBox("idField").enterText("1");
		window.textBox("nameField").enterText("Laptop");
		window.textBox("quantityField").enterText("10");
		window.button("addButton").requireDisabled();
		window.textBox("priceField").enterText("999.99");
		window.button("addButton").requireEnabled();
	}

	@Test
	public void testAddButtonNotifiesControllerWithProductFromTextFields() {
		window.textBox("idField").setText("1");
		window.textBox("nameField").setText("Laptop");
		window.textBox("quantityField").setText("10");
		window.textBox("priceField").setText("999.99");
		window.button("addButton").click();
		ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
		verify(productController, timeout(1000)).newProduct(productCaptor.capture());
		assertProduct(productCaptor.getValue(), "1", "Laptop", 10, 999.99);
	}

	@Test
	public void testUpdateButtonNotifiesControllerWithProductFromTextFields() {
		window.textBox("idField").setText("1");
		window.textBox("nameField").setText("Laptop");
		window.textBox("quantityField").setText("15");
		window.textBox("priceField").setText("899.99");
		window.button("updateButton").click();
		ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
		verify(productController, timeout(1000)).updateProduct(productCaptor.capture());
		assertProduct(productCaptor.getValue(), "1", "Laptop", 15, 899.99);
	}

	@Test
	public void testDeleteButtonNotifiesControllerWithSelectedProduct() {
		GuiActionRunner.execute(() ->
			view.showAllProducts(List.of(new Product("1", "Laptop", 10, 999.99))));
		window.table("productTable").selectRows(0);
		window.button("deleteButton").click();
		ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
		verify(productController, timeout(1000)).deleteProduct(productCaptor.capture());
		assertProduct(productCaptor.getValue(), "1", "Laptop", 10, 999.99);
	}

	@Test
	public void testRefreshButtonNotifiesController() {
		GuiActionRunner.execute(() -> window.button(JButtonMatcher.withText("Refresh")).target().doClick());
		verify(productController, timeout(1000)).allProducts();
	}

	@Test
	public void testShowAllProducts() {
		GuiActionRunner.execute(() ->
			view.showAllProducts(List.of(
				new Product("1", "Laptop", 10, 999.99),
				new Product("2", "Mouse", 20, 19.99))));
		String[][] contents = window.table("productTable").contents();
		assertThat(contents).isDeepEqualTo(new String[][] {
			{ "1", "Laptop", "10", "999.99" },
			{ "2", "Mouse", "20", "19.99" }
		});
	}

	private void assertProduct(Product product, String id, String name, int quantity, double price) {
		assertThat(product.getId()).isEqualTo(id);
		assertThat(product.getName()).isEqualTo(name);
		assertThat(product.getQuantity()).isEqualTo(quantity);
		assertThat(product.getPrice()).isEqualTo(price);
	}
}
