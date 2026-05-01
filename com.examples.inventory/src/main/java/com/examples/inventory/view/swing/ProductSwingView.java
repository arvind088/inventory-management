package com.examples.inventory.view.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import com.examples.inventory.controller.ProductController;
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
	private JButton refreshButton;
	private JLabel messageLabel;
	private transient ProductController productController;

	public ProductSwingView() {
		setTitle("Inventory Management");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 430);
		JPanel contentPane = new JPanel(new BorderLayout(10, 10));
		contentPane.setBorder(new EmptyBorder(8, 10, 8, 10));
		setContentPane(contentPane);
		add(createTitleLabel(), BorderLayout.NORTH);
		add(createMainPanel(), BorderLayout.CENTER);
	}

	public void setProductController(ProductController productController) {
		this.productController = productController;
	}

	private JLabel createTitleLabel() {
		JLabel titleLabel = new JLabel("Inventory Management", SwingConstants.CENTER);
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 15F));
		titleLabel.setOpaque(true);
		titleLabel.setBackground(new Color(65, 72, 78));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setBorder(new EmptyBorder(5, 0, 5, 0));
		return titleLabel;
	}

	private JPanel createMainPanel() {
		JPanel mainPanel = new JPanel(new BorderLayout(8, 8));
		mainPanel.add(createAddPanel(), BorderLayout.NORTH);
		mainPanel.add(createTablePanel(), BorderLayout.CENTER);
		mainPanel.add(createActionsPanel(), BorderLayout.SOUTH);
		return mainPanel;
	}

	private JScrollPane createTablePanel() {
		productTable = new JTable(new DefaultTableModel(
			new Object[] { "Id", "Name", "Quantity", "Price" }, 0));
		productTable.setName("productTable");
		productTable.setFillsViewportHeight(true);
		productTable.setRowHeight(24);
		productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane tableScrollPane = new JScrollPane(productTable);
		tableScrollPane.setBorder(BorderFactory.createTitledBorder("Products"));
		return tableScrollPane;
	}

	private JPanel createAddPanel() {
		JPanel addPanel = new JPanel(new BorderLayout(5, 5));
		addPanel.setBorder(BorderFactory.createTitledBorder("Add Product"));
		JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 8, 3));
		idTextField = createTextField("idField");
		nameTextField = createTextField("nameField");
		quantityTextField = createTextField("quantityField");
		priceTextField = createTextField("priceField");
		addButton = createButton("addButton", "Add");
		addButton.setEnabled(false);
		addButton.addActionListener(e -> productController.newProduct(productFromFields()));
		addTextFieldListeners();
		fieldsPanel.add(new JLabel("Id"));
		fieldsPanel.add(idTextField);
		fieldsPanel.add(new JLabel("Name"));
		fieldsPanel.add(nameTextField);
		fieldsPanel.add(new JLabel("Quantity"));
		fieldsPanel.add(quantityTextField);
		fieldsPanel.add(new JLabel("Price"));
		fieldsPanel.add(priceTextField);
		addPanel.add(fieldsPanel, BorderLayout.CENTER);
		addPanel.add(addButton, BorderLayout.SOUTH);
		return addPanel;
	}

	private JPanel createActionsPanel() {
		JPanel actionsPanel = new JPanel(new BorderLayout(5, 4));
		JPanel buttonsPanel = new JPanel(new GridLayout(1, 3, 6, 0));
		updateButton = createButton("updateButton", "Update");
		deleteButton = createButton("deleteButton", "Delete");
		refreshButton = createButton("refreshButton", "Refresh");
		updateButton.addActionListener(e -> productController.updateProduct(productFromFields()));
		deleteButton.addActionListener(e -> {
			Product selectedProduct = selectedProductFromTable();
			if (selectedProduct != null) {
				productController.deleteProduct(selectedProduct);
			}
		});
		refreshButton.addActionListener(e -> productController.allProducts());
		buttonsPanel.add(updateButton);
		buttonsPanel.add(deleteButton);
		buttonsPanel.add(refreshButton);
		messageLabel = new JLabel(" ");
		messageLabel.setName("messageLabel");
		actionsPanel.add(buttonsPanel, BorderLayout.CENTER);
		actionsPanel.add(messageLabel, BorderLayout.SOUTH);
		return actionsPanel;
	}

	private JTextField createTextField(String name) {
		JTextField textField = new JTextField();
		textField.setName(name);
		textField.setColumns(10);
		return textField;
	}

	private JButton createButton(String name, String text) {
		JButton button = new JButton(text);
		button.setName(name);
		return button;
	}

	private void addTextFieldListeners() {
		DocumentListener listener = new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateAddButton();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateAddButton();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateAddButton();
			}
		};
		idTextField.getDocument().addDocumentListener(listener);
		nameTextField.getDocument().addDocumentListener(listener);
		quantityTextField.getDocument().addDocumentListener(listener);
		priceTextField.getDocument().addDocumentListener(listener);
	}

	private void updateAddButton() {
		addButton.setEnabled(
			!idTextField.getText().isBlank() &&
			!nameTextField.getText().isBlank() &&
			!quantityTextField.getText().isBlank() &&
			!priceTextField.getText().isBlank());
	}

	private Product productFromFields() {
		return new Product(
			idTextField.getText(),
			nameTextField.getText(),
			Integer.parseInt(quantityTextField.getText()),
			Double.parseDouble(priceTextField.getText()));
	}

	private Product selectedProductFromTable() {
		int selectedRow = productTable.getSelectedRow();
		if (selectedRow == -1) {
			return null;
		}
		DefaultTableModel tableModel = (DefaultTableModel) productTable.getModel();
		return new Product(
			tableModel.getValueAt(selectedRow, 0).toString(),
			tableModel.getValueAt(selectedRow, 1).toString(),
			Integer.parseInt(tableModel.getValueAt(selectedRow, 2).toString()),
			Double.parseDouble(tableModel.getValueAt(selectedRow, 3).toString()));
	}

	@Override
	public void showAllProducts(List<Product> products) {
		DefaultTableModel tableModel = (DefaultTableModel) productTable.getModel();
		tableModel.setRowCount(0);
		products.forEach(this::addProductToTable);
	}

	@Override
	public void productAdded(Product product) {
		addProductToTable(product);
		messageLabel.setForeground(Color.BLACK);
		messageLabel.setText("Added product with id " + product.getId());
	}

	@Override
	public void productUpdated(Product product) {
		int row = findRowByProductId(product.getId());
		if (row != -1) {
			DefaultTableModel tableModel = (DefaultTableModel) productTable.getModel();
			tableModel.setValueAt(product.getId(), row, 0);
			tableModel.setValueAt(product.getName(), row, 1);
			tableModel.setValueAt(product.getQuantity(), row, 2);
			tableModel.setValueAt(product.getPrice(), row, 3);
		}
		messageLabel.setForeground(Color.BLACK);
		messageLabel.setText("Updated product with id " + product.getId());
	}

	@Override
	public void productRemoved(Product product) {
		int row = findRowByProductId(product.getId());
		if (row != -1) {
			((DefaultTableModel) productTable.getModel()).removeRow(row);
		}
		messageLabel.setForeground(Color.BLACK);
		messageLabel.setText("Deleted product with id " + product.getId());
	}

	@Override
	public void showError(String message, Product product) {
		messageLabel.setForeground(Color.RED);
		messageLabel.setText(message);
	}

	@Override
	public void showErrorProductNotFound(String message, Product product) {
		messageLabel.setForeground(Color.RED);
		messageLabel.setText(message);
	}

	private void addProductToTable(Product product) {
		((DefaultTableModel) productTable.getModel()).addRow(new Object[] {
			product.getId(),
			product.getName(),
			product.getQuantity(),
			product.getPrice()
		});
	}

	private int findRowByProductId(String id) {
		DefaultTableModel tableModel = (DefaultTableModel) productTable.getModel();
		for (int row = 0; row < tableModel.getRowCount(); row++) {
			if (id.equals(tableModel.getValueAt(row, 0))) {
				return row;
			}
		}
		return -1;
	}
}
