package com.examples.inventory.model;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ItemTest {

	@Test
	void testEqualsAndHashCode() {
		Item a = new Item("SKU1", "Lenovo", 10);
		Item b = new Item("SKU1", "Lenovo", 10);
		
		assertThat(a).isEqualTo(b);
		assertThat(a.hashCode()).isEqualTo(b.hashCode());
		}
}
