package com.github.honatas.fieldvalidator.exception;

import java.util.Map;

/**
 * An exception to be thrown in case of having field validation errors. It carries the errors.
 */
public class FieldValidationException extends Exception {

	private static final long serialVersionUID = -5275417944027609971L;

	private Map<String, String> errors;
	
	public FieldValidationException(Map<String, String> errors) {
		this.errors = errors;
	}
	
	/** @return A {@code Map} conatining an error message for each field. */
	public Map<String, String> getErrors() {
		return this.errors;
	}
}
