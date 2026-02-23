package com.github.honatas.fieldvalidator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FieldValidatorTest {

	@Test
	void testGetErrors_returnsEmptyMapInitially() {
		TestValidator v = new TestValidator();
		assertTrue(v.getErrors().isEmpty());
	}
	
	@Test
	void testValidate_dataInline_passesRequired() {
		TestValidator v = new TestValidator();
		v.validate("John", "name", v.required);
		assertTrue(v.getErrors().isEmpty());
	}

	@Test
	void testValidate_dataInline_failsRequired() {
		TestValidator v = new TestValidator();
		v.validate(null, "name", v.required);
		assertTrue(v.getErrors().containsKey("name"));
	}

	@Test
	void testValidate_pojoInline_passesRequired() {
		TestPojo pojo = new TestPojo("John");
		TestValidator v = new TestValidator();
		v.validate(pojo.getName(), "name", v.required);
		assertTrue(v.getErrors().isEmpty());
	}

	@Test
	void testValidate_pojoInline_failsRequired() {
		TestPojo pojo = new TestPojo(null);
		TestValidator v = new TestValidator();
		v.validate(pojo.getName(), "name", v.required);
		assertTrue(v.getErrors().containsKey("name"));
	}

	@Test
	void testValidate_recordInline_passesRequired() {
		TestRecord rec = new TestRecord("John");
		TestValidator v = new TestValidator();
		v.validate(rec.name(), "name", v.required);
		assertTrue(v.getErrors().isEmpty());
	}

	@Test
	void testValidate_recordInline_failsRequired() {
		TestRecord rec = new TestRecord(null);
		TestValidator v = new TestValidator();
		v.validate(rec.name(), "name", v.required);
		assertTrue(v.getErrors().containsKey("name"));
	}

	@Test
	void testValidate_pojo_passesRequired() {
		TestPojo pojo = new TestPojo("John");
		TestValidator v = new TestValidator(pojo);
		v.validate("name", v.required);
		assertTrue(v.getErrors().isEmpty());
	}

	@Test
	void testValidate_pojo_failsRequired() {
		TestPojo pojo = new TestPojo(null);
		TestValidator v = new TestValidator(pojo);
		v.validate("name", v.required);
		assertTrue(v.getErrors().containsKey("name"));
	}

	@Test
	void testValidate_record_passesRequired() {
		TestRecord rec = new TestRecord("John");
		TestValidator v = new TestValidator(rec);
		v.validate("name", v.required);
		assertTrue(v.getErrors().isEmpty());
	}

	@Test
	void testValidate_record_failsRequired() {
		TestRecord rec = new TestRecord(null);
		TestValidator v = new TestValidator(rec);
		v.validate("name", v.required);
		assertTrue(v.getErrors().containsKey("name"));
	}

	@Test
	void documentationTest() {
		TestRecord data = new TestRecord(null, 2, -3);
		TestValidator v = new TestValidator(data);
		v.validate("name", v.required);
		v.validate("age", v.required, v.positive);
		v.validate("height", v.required, v.positive);
		assertEquals("{name=name is required, height=height must be greater than zero}", v.getErrors().toString());
	}

	@Test
	void edgeCases() {
		TestValidator v = new TestValidator();
		// No validators
		assertThrows(IllegalArgumentException.class, () -> {
			v.validate("abc", "name");
		});
		// Empty field name
		assertThrows(IllegalArgumentException.class, () -> {
			v.validate("abc", "", v.required);
		});
		// No data
		assertThrows(IllegalStateException.class, () -> {
			v.validate("name", v.required);
		});
	}
}
