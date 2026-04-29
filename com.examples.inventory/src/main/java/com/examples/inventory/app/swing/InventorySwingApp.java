package com.examples.inventory.app.swing;

import java.awt.EventQueue;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.examples.inventory.controller.ProductController;
import com.examples.inventory.repository.mongo.MongoProductRepository;
import com.examples.inventory.view.swing.ProductSwingView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public class InventorySwingApp implements Callable<Void> {

	@Option(names = { "--mongo-host" }, description = "MongoDB host address")
	private String mongoHost = "localhost";

	@Option(names = { "--mongo-port" }, description = "MongoDB host port")
	private int mongoPort = 27017;

	@Option(names = { "--db-name" }, description = "Database name")
	private String databaseName = "inventory";

	@Option(names = { "--db-collection" }, description = "Collection name")
	private String collectionName = "product";

	public static void main(String[] args) {
		new CommandLine(new InventorySwingApp()).execute(args);
	}

	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {
			try {
				MongoProductRepository productRepository =
					new MongoProductRepository(
						new MongoClient(new ServerAddress(mongoHost, mongoPort)),
						databaseName,
						collectionName);
				ProductSwingView productSwingView = new ProductSwingView();
				ProductController productController =
					new ProductController(productSwingView, productRepository);
				productSwingView.setProductController(productController);
				productSwingView.setVisible(true);
				productController.allProducts();
			} catch (Exception e) {
				Logger.getLogger(getClass().getName())
					.log(Level.SEVERE, "Exception", e);
			}
		});
		return null;
	}
}
