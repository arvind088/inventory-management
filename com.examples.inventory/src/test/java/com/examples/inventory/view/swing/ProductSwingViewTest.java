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
