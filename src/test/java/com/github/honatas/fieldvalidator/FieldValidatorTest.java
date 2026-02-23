package com.github.honatas.fieldvalidator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FieldValidatorTest {

	@Test
	void testGetErrors_returnsEmptyMapInitially() {
		TestValidator validator = new TestValidator();
		assertTrue(validator.getErrors().isEmpty());
	}
	
	@Test
	void testValidate_dataInline_passesRequired() {
		TestValidator validator = new TestValidator();
		validator.validate("John", "name", validator.required);
		assertTrue(validator.getErrors().isEmpty());
	}

	@Test
	void testValidate_dataInline_failsRequired() {
		TestValidator validator = new TestValidator();
		validator.validate(null, "name", validator.required);
		assertTrue(validator.getErrors().containsKey("name"));
	}

	@Test
	void testValidate_pojoInline_passesRequired() {
		TestPojo pojo = new TestPojo("John");
		TestValidator validator = new TestValidator();
		validator.validate(pojo.getName(), "name", validator.required);
		assertTrue(validator.getErrors().isEmpty());
	}

	@Test
	void testValidate_pojoInline_failsRequired() {
		TestPojo pojo = new TestPojo(null);
		TestValidator validator = new TestValidator();
		validator.validate(pojo.getName(), "name", validator.required);
		assertTrue(validator.getErrors().containsKey("name"));
	}

	@Test
	void testValidate_recordInline_passesRequired() {
		TestRecord rec = new TestRecord("John");
		TestValidator validator = new TestValidator();
		validator.validate(rec.name(), "name", validator.required);
		assertTrue(validator.getErrors().isEmpty());
	}

	@Test
	void testValidate_recordInline_failsRequired() {
		TestRecord rec = new TestRecord(null);
		TestValidator validator = new TestValidator();
		validator.validate(rec.name(), "name", validator.required);
		assertTrue(validator.getErrors().containsKey("name"));
	}

	@Test
	void testValidate_pojo_passesRequired() {
		TestPojo pojo = new TestPojo("John");
		TestValidator validator = new TestValidator(pojo);
		validator.validate("name", validator.required);
		assertTrue(validator.getErrors().isEmpty());
	}

	@Test
	void testValidate_pojo_failsRequired() {
		TestPojo pojo = new TestPojo(null);
		TestValidator validator = new TestValidator(pojo);
		validator.validate("name", validator.required);
		assertTrue(validator.getErrors().containsKey("name"));
	}

	@Test
	void testValidate_record_passesRequired() {
		TestRecord rec = new TestRecord("John");
		TestValidator validator = new TestValidator(rec);
		validator.validate("name", validator.required);
		assertTrue(validator.getErrors().isEmpty());
	}

	@Test
	void testValidate_record_failsRequired() {
		TestRecord rec = new TestRecord(null);
		TestValidator validator = new TestValidator(rec);
		validator.validate("name", validator.required);
		assertTrue(validator.getErrors().containsKey("name"));
	}

	@Test
	void documentationTest() {
		TestRecord data = new TestRecord(null, 2, -3);
		TestValidator validator = new TestValidator(data);
		validator.validate("name", validator.required);
		validator.validate("age", validator.required, validator.positive);
		validator.validate("height", validator.required, validator.positive);
		assertEquals("{name=name is required, height=height must be greater than zero}", validator.getErrors().toString());
	}
}
