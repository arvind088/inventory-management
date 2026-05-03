package com.examples.inventory.repository.mongo;

import java.util.List;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.examples.inventory.model.Product;
import com.examples.inventory.repository.ProductRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class MongoProductRepository implements ProductRepository {

	private MongoCollection<Document> productCollection;

	public MongoProductRepository(MongoClient client, String databaseName, String collectionName) {
		productCollection = client
			.getDatabase(databaseName)
			.getCollection(collectionName);
	}

	@Override
	public void save(Product product) {
		productCollection.insertOne(fromProductToDocument(product));
	}

	@Override
	public List<Product> findAll() {
		return StreamSupport.
			stream(productCollection.find().spliterator(), false)
				.map(this::fromDocumentToProduct)
				.toList();
	}

	private Product fromDocumentToProduct(Document d) {
		return new Product(
			"" + d.get("id"),
			"" + d.get("name"),
			(int) d.get("quantity"),
			(double) d.get("price"));
	}

	private Document fromProductToDocument(Product product) {
		return new Document()
			.append("id", product.getId())
			.append("name", product.getName())
			.append("quantity", product.getQuantity())
			.append("price", product.getPrice());
	}

	@Override
	public Product findById(String id) {
		Document d = productCollection.find(Filters.eq("id", id)).first();
		if (d != null) {
			return fromDocumentToProduct(d);
		}
		return null;
	}

	@Override
	public void update(Product product) {
		productCollection.replaceOne(
			Filters.eq("id", product.getId()),
			fromProductToDocument(product));
	}

	@Override
	public void delete(String id) {
		productCollection.deleteOne(Filters.eq("id", id));
	}
}
