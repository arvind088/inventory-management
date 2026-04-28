package com.examples.inventory.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

import com.examples.inventory.model.Product;

public class ProductSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;
	private ProductSwingView view;

	@Override
	protected void onSetUp() {
		view = GuiActionRunner.execute(new GuiQuery<ProductSwingView>() {
			@Override
			protected ProductSwingView executeInEDT() {
				return new ProductSwingView();
			}
		});
		window = new FrameFixture(robot(), view);
		window.show();
	}

	@Test
	public void testInitialControls() {
		window.table("productTable");
		window.textBox("idTextField");
		window.textBox("nameTextField");
		window.textBox("quantityTextField");
		window.textBox("priceTextField");
		window.button("addButton").requireText("Add");
		window.button("updateButton").requireText("Update");
		window.button("deleteButton").requireText("Delete");
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
}
