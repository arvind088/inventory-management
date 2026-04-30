package com.examples.inventory.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MongoDBContainer;

import com.examples.inventory.controller.ProductController;
import com.examples.inventory.model.Product;
import com.examples.inventory.repository.mongo.MongoProductRepository;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class InventorySwingAppE2E extends AssertJSwingJUnitTestCase {

	@ClassRule
	public static final MongoDBContainer mongo =
		new MongoDBContainer("mongo:5");

	private static final String INVENTORY_DB_NAME = "inventory";
	private static final String PRODUCT_COLLECTION_NAME = "product";

	private FrameFixture window;
	private ProductSwingView view;
	private MongoClient client;
	private MongoProductRepository productRepository;

	@Override
	protected void onSetUp() {
		client = new MongoClient(
			new ServerAddress(
				mongo.getHost(),
				mongo.getFirstMappedPort()));
		client.getDatabase(INVENTORY_DB_NAME).drop();
		productRepository =
			new MongoProductRepository(client, INVENTORY_DB_NAME, PRODUCT_COLLECTION_NAME);
		view = GuiActionRunner.execute(new GuiQuery<ProductSwingView>() {
			@Override
			protected ProductSwingView executeInEDT() {
				ProductSwingView productSwingView = new ProductSwingView();
				ProductController productController =
					new ProductController(productSwingView, productRepository);
				productSwingView.setProductController(productController);
				productController.allProducts();
				return productSwingView;
			}
		});
		window = new FrameFixture(robot(), view);
		window.show();
	}

	@After
	public void closeMongoClient() {
		client.close();
	}

	@Test
	public void testAddUpdateAndDeleteProductFromSwingViewToMongo() {
		fillProductFields("1", "Laptop", "10", "999.99");
		window.button("addButton").click();
		assertTableContainsExactly(new String[][] {
			{ "1", "Laptop", "10", "999.99" }
		});
		assertThat(productRepository.findById("1"))
			.usingRecursiveComparison()
			.isEqualTo(new Product("1", "Laptop", 10, 999.99));

		fillProductFields("1", "Gaming Laptop", "5", "1299.99");
		window.button("updateButton").click();
		assertTableContainsExactly(new String[][] {
			{ "1", "Gaming Laptop", "5", "1299.99" }
		});
		assertThat(productRepository.findById("1"))
			.usingRecursiveComparison()
			.isEqualTo(new Product("1", "Gaming Laptop", 5, 1299.99));

		window.table("productTable").selectRows(0);
		window.button("deleteButton").click();
		assertTableContainsExactly(new String[][] {});
		assertThat(productRepository.findById("1"))
			.isNull();
	}

	private void fillProductFields(String id, String name, String quantity, String price) {
		window.textBox("idField").setText(id);
		window.textBox("nameField").setText(name);
		window.textBox("quantityField").setText(quantity);
		window.textBox("priceField").setText(price);
	}

	private void assertTableContainsExactly(String[][] expectedContents) {
		assertThat(window.table("productTable").contents())
			.isDeepEqualTo(expectedContents);
	}
}
