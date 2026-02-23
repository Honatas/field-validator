package com.github.honatas.fieldvalidator.exception;

import java.io.Serializable;
import java.util.Map;

/**
 * An exception to be thrown in case of having field validation errors. It carries the errors.
 */
public class FieldValidationException extends Exception {

	private static final long serialVersionUID = -5275417944027609971L;

	private final Map<String, String> errors;
	private final Serializable data;

	public FieldValidationException(Map<String, String> errors, Serializable data) {
		this.errors = errors;
		this.data = data;
	}
	
	public FieldValidationException(Map<String, String> errors) {
		this(errors, null);
	}
	
	/** @return A {@code Map} conatining an error message for each field. */
	public Map<String, String> getErrors() {
		return this.errors;
	}

	/** @return The data that was just validated. */
	public Serializable getData() {
		return this.data;
	}
}
