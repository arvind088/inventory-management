package com.examples.inventory.view.swing;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

public class ProductSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;

	@Override
	protected void onSetUp() {
		ProductSwingView view = GuiActionRunner.execute(new GuiQuery<ProductSwingView>() {
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
}
