package br.com.honatas.fieldvalidator.exception;

import java.util.Map;

public class FieldValidationException extends Exception {

	private static final long serialVersionUID = -5275417944027609971L;

	private Map<String, String> errors;
	
	public FieldValidationException(Map<String, String> errors) {
		this.errors = errors;
	}
	
	public Map<String, String> getErrors() {
		return this.errors;
	}
}
