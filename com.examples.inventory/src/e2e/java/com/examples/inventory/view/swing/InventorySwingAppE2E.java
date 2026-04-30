package com.examples.inventory.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

import javax.swing.JFrame;

import org.bson.Document;
import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;

import com.examples.inventory.model.Product;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

@RunWith(GUITestRunner.class)
public class InventorySwingAppE2E extends AssertJSwingJUnitTestCase {

	@ClassRule
	public static final MongoDBContainer mongo =
		new MongoDBContainer("mongo:5");

	private static final String INVENTORY_DB_NAME = "inventory";
	private static final String PRODUCT_COLLECTION_NAME = "product";

	private FrameFixture window;
	private MongoClient client;
	private MongoCollection<Document> productCollection;

	@Override
	protected void onSetUp() {
		client = new MongoClient(
			new ServerAddress(
				mongo.getHost(),
				mongo.getFirstMappedPort()));
		client.getDatabase(INVENTORY_DB_NAME).drop();
		productCollection = client
			.getDatabase(INVENTORY_DB_NAME)
			.getCollection(PRODUCT_COLLECTION_NAME);

		application("com.examples.inventory.app.swing.InventorySwingApp")
			.withArgs(
				"--mongo-host=" + mongo.getHost(),
				"--mongo-port=" + mongo.getFirstMappedPort(),
				"--db-name=" + INVENTORY_DB_NAME,
				"--db-collection=" + PRODUCT_COLLECTION_NAME)
			.start();

		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Inventory Management".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());
	}

	@After
	public void closeMongoClient() {
		client.close();
	}

	@Test @GUITest
	public void testAddButtonSuccess() {
		fillProductFields("1", "Laptop", "10", "999.99");
		window.button(JButtonMatcher.withText("Add")).click();
		assertTableContainsExactly(new String[][] {
			{ "1", "Laptop", "10", "999.99" }
		});
		assertThat(findProductById("1"))
			.usingRecursiveComparison()
			.isEqualTo(new Product("1", "Laptop", 10, 999.99));
	}

	@Test @GUITest
	public void testUpdateButtonSuccess() {
		addTestProductToDatabase("1", "Laptop", 10, 999.99);
		window.button(JButtonMatcher.withText("Refresh")).click();

		fillProductFields("1", "Gaming Laptop", "5", "1299.99");
		window.button(JButtonMatcher.withText("Update")).click();
		assertTableContainsExactly(new String[][] {
			{ "1", "Gaming Laptop", "5", "1299.99" }
		});
		assertThat(findProductById("1"))
			.usingRecursiveComparison()
			.isEqualTo(new Product("1", "Gaming Laptop", 5, 1299.99));
	}

	@Test @GUITest
	public void testDeleteButtonSuccess() {
		addTestProductToDatabase("1", "Laptop", 10, 999.99);
		window.button(JButtonMatcher.withText("Refresh")).click();

		window.table("productTable").selectRows(0);
		window.button(JButtonMatcher.withText("Delete")).click();
		assertTableContainsExactly(new String[][] {});
		assertThat(findProductById("1"))
			.isNull();
	}

	private void fillProductFields(String id, String name, String quantity, String price) {
		window.textBox("idField").setText("");
		window.textBox("idField").enterText(id);
		window.textBox("nameField").setText("");
		window.textBox("nameField").enterText(name);
		window.textBox("quantityField").setText("");
		window.textBox("quantityField").enterText(quantity);
		window.textBox("priceField").setText("");
		window.textBox("priceField").enterText(price);
	}

	private void assertTableContainsExactly(String[][] expectedContents) {
		assertThat(window.table("productTable").contents())
			.isDeepEqualTo(expectedContents);
	}

	private void addTestProductToDatabase(String id, String name, int quantity, double price) {
		productCollection.insertOne(
			new Document()
				.append("id", id)
				.append("name", name)
				.append("quantity", quantity)
				.append("price", price));
	}

	private Product findProductById(String id) {
		Document productDocument = productCollection.find(Filters.eq("id", id)).first();
		if (productDocument == null) {
			return null;
		}
		return new Product(
			"" + productDocument.get("id"),
			"" + productDocument.get("name"),
			(int) productDocument.get("quantity"),
			(double) productDocument.get("price"));
	}
}
