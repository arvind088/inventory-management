package com.examples.inventory.repository.mongo;

import java.util.List;
import java.util.stream.Collectors;
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
		productCollection.insertOne(
			new Document()
				.append("id", product.getId())
				.append("name", product.getName())
				.append("quantity", product.getQuantity())
				.append("price", product.getPrice()));
	}

	@Override
	public List<Product> findAll() {
		return StreamSupport.
			stream(productCollection.find().spliterator(), false)
				.map(this::fromDocumentToProduct)
				.collect(Collectors.toList());
	}

	private Product fromDocumentToProduct(Document d) {
		return new Product(
			"" + d.get("id"),
			"" + d.get("name"),
			(int) d.get("quantity"),
			(double) d.get("price"));
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
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void delete(String id) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
