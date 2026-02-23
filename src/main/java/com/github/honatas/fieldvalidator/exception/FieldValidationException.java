package com.github.honatas.fieldvalidator.exception;

import java.io.Serializable;
import java.util.Map;

/**
 * An exception to be thrown in case of having field validation errors. It carries the errors and the tested data, if it exists.
 */
public class FieldValidationException extends Exception {

	private static final long serialVersionUID = -5275417944027609971L;

	/** The errors map. */
	private final Map<String, String> errors;
	/** The data that was validated. */
	private final Serializable data;

	/** 
	 * Creates a {@code FieldValidationException} with the given errors and data.
	 * 
	 * @param errors the errors map
	 * @param data the data that has been passed to the constructor
	 */
	public FieldValidationException(Map<String, String> errors, Serializable data) {
		this.errors = errors;
		this.data = data;
	}
	
	/** 
	 * Creates a {@code FieldValidationException} with the given errors and no data.
	 * @param errors the errors map
	 */
	public FieldValidationException(Map<String, String> errors) {
		this(errors, null);
	}
	
	/** 
	 * Returns the errors map.
	 * @return A {@code Map} conatining an error message for each field. 
	 */
	public Map<String, String> getErrors() {
		return this.errors;
	}

	/** 
	 * Returns the data passed to the constructor.
	 * @return The data that was just validated. 
	 */
	public Serializable getData() {
		return this.data;
	}
}
