package com.examples.inventory.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MongoDBContainer;

import com.examples.inventory.model.Product;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoProductRepositoryTestcontainersIT {

	@ClassRule
	public static final MongoDBContainer mongo =
		new MongoDBContainer("mongo:5");

	private MongoClient client;
	private MongoProductRepository productRepository;
	private MongoCollection<Document> productCollection;

	private static final String INVENTORY_DB_NAME = "inventory";
	private static final String PRODUCT_COLLECTION_NAME = "product";

	@Before
	public void setup() {
		client = new MongoClient(
			new ServerAddress(
				mongo.getHost(),
				mongo.getFirstMappedPort()));
		productRepository =
			new MongoProductRepository(client, INVENTORY_DB_NAME, PRODUCT_COLLECTION_NAME);
		MongoDatabase database = client.getDatabase(INVENTORY_DB_NAME);
		database.drop();
		productCollection = database.getCollection(PRODUCT_COLLECTION_NAME);
	}

	@After
	public void tearDown() {
		client.close();
	}

	@Test
	public void testSave() {
		Product product = new Product("1", "Laptop", 10, 999.99);
		productRepository.save(product);
		assertThat(readAllProductsFromDatabase())
			.usingRecursiveFieldByFieldElementComparator()
			.containsExactly(product);
	}

	@Test
	public void testFindAll() {
		addTestProductToDatabase("1", "Laptop", 10, 999.99);
		addTestProductToDatabase("2", "Mouse", 20, 19.99);
		assertThat(productRepository.findAll())
			.usingRecursiveFieldByFieldElementComparator()
			.containsExactly(
				new Product("1", "Laptop", 10, 999.99),
				new Product("2", "Mouse", 20, 19.99));
	}

	@Test
	public void testFindByIdWhenProductExists() {
		addTestProductToDatabase("1", "Laptop", 10, 999.99);
		addTestProductToDatabase("2", "Mouse", 20, 19.99);
		assertThat(productRepository.findById("2"))
			.usingRecursiveComparison()
			.isEqualTo(new Product("2", "Mouse", 20, 19.99));
	}

	@Test
	public void testFindByIdWhenProductDoesNotExist() {
		addTestProductToDatabase("1", "Laptop", 10, 999.99);
		assertThat(productRepository.findById("2"))
			.isNull();
	}

	@Test
	public void testUpdate() {
		addTestProductToDatabase("1", "Laptop", 10, 999.99);
		Product editedProduct = new Product("1", "Gaming Laptop", 5, 1299.99);
		productRepository.update(editedProduct);
		assertThat(readAllProductsFromDatabase())
			.usingRecursiveFieldByFieldElementComparator()
			.containsExactly(editedProduct);
	}

	@Test
	public void testDelete() {
		addTestProductToDatabase("1", "Laptop", 10, 999.99);
		addTestProductToDatabase("2", "Mouse", 20, 19.99);
		productRepository.delete("1");
		assertThat(readAllProductsFromDatabase())
			.usingRecursiveFieldByFieldElementComparator()
			.containsExactly(new Product("2", "Mouse", 20, 19.99));
	}

	private void addTestProductToDatabase(String id, String name, int quantity, double price) {
		productCollection.insertOne(
			new Document()
				.append("id", id)
				.append("name", name)
				.append("quantity", quantity)
				.append("price", price));
	}

	private List<Product> readAllProductsFromDatabase() {
		return StreamSupport.
			stream(productCollection.find().spliterator(), false)
				.map(d -> new Product(
					"" + d.get("id"),
					"" + d.get("name"),
					(int) d.get("quantity"),
					(double) d.get("price")))
				.toList();
	}
}
