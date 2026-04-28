package com.examples.inventory.view.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.examples.inventory.model.Product;
import com.examples.inventory.view.ProductView;

public class ProductSwingView extends JFrame implements ProductView {

	private static final long serialVersionUID = 1L;

	private JTable productTable;
	private JTextField idTextField;
	private JTextField nameTextField;
	private JTextField quantityTextField;
	private JTextField priceTextField;
	private JButton addButton;
	private JButton updateButton;
	private JButton deleteButton;

	public ProductSwingView() {
		setTitle("Inventory Management");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 400);
		add(createTablePanel(), BorderLayout.CENTER);
		add(createFormPanel(), BorderLayout.SOUTH);
	}

	private JScrollPane createTablePanel() {
		productTable = new JTable(new DefaultTableModel(
			new Object[] { "Id", "Name", "Quantity", "Price" }, 0));
		productTable.setName("productTable");
		return new JScrollPane(productTable);
	}

	private JPanel createFormPanel() {
		JPanel formPanel = new JPanel(new GridLayout(2, 4));
		idTextField = createTextField("idTextField");
		nameTextField = createTextField("nameTextField");
		quantityTextField = createTextField("quantityTextField");
		priceTextField = createTextField("priceTextField");
		addButton = createButton("addButton", "Add");
		updateButton = createButton("updateButton", "Update");
		deleteButton = createButton("deleteButton", "Delete");
		formPanel.add(idTextField);
		formPanel.add(nameTextField);
		formPanel.add(quantityTextField);
		formPanel.add(priceTextField);
		formPanel.add(addButton);
		formPanel.add(updateButton);
		formPanel.add(deleteButton);
		return formPanel;
	}

	private JTextField createTextField(String name) {
		JTextField textField = new JTextField();
		textField.setName(name);
		return textField;
	}

	private JButton createButton(String name, String text) {
		JButton button = new JButton(text);
		button.setName(name);
		return button;
	}

	@Override
	public void showAllProducts(List<Product> products) {
		DefaultTableModel tableModel = (DefaultTableModel) productTable.getModel();
		tableModel.setRowCount(0);
		products.forEach(product -> tableModel.addRow(new Object[] {
			product.getId(),
			product.getName(),
			product.getQuantity(),
			product.getPrice()
		}));
	}

	@Override
	public void productAdded(Product product) {
	}

	@Override
	public void productUpdated(Product product) {
	}

	@Override
	public void productRemoved(Product product) {
	}

	@Override
	public void showError(String message, Product product) {
	}

	@Override
	public void showErrorProductNotFound(String message, Product product) {
	}
}
