package com.github.honatas.fieldvalidator;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.github.honatas.fieldvalidator.exception.FieldValidationException;

/**
 * <p>Provides means for registering validators and validate data with them.</p>
 * 
 * @author Jonatas "Honatas" de Moraes Junior
 */
public class FieldValidator {

	protected Map<String, String> errors = new HashMap<>();
	protected Serializable data;
	protected Class<?> dataClazz;

	public FieldValidator() {
	}

	public FieldValidator(Serializable data) {
		this.data = data;
		this.dataClazz = data.getClass();
	}
	
	/** @return A {@code java.util.Map} conatining an error message for each field. */
	public Map<String, String> getErrors() {
		return this.errors;
	}

	/** @return The data that is being validated. */
	public Serializable getData() {
		return this.data;
	}
	
	/** @return true if a validation error has occurred, false otherwise */
	public boolean hasErrors() {
		return !this.errors.isEmpty();
	}
	
	/** Checks if has any error and throws a {@code FieldValidationException} when errors are found. */
	public void checkHasErrors() throws FieldValidationException {
		if (this.hasErrors()) {
			throw new FieldValidationException(this.errors, this.data);
		}
	}
	
	/** @return true if there is an error for the specified field. */
	public boolean hasErrorOn(String field) {
		return this.getErrors().get(field) != null;
	}
	
	/** Explicitly adds an error on the specified field. Use it if you need FieldValidator to output the result of external validations. */
	public void addErrorOn(String field, String error) {
		this.getErrors().put(field, error);
	}
	
	/**
	 * Validator functional interface, represents a validation method.
	 */
	public interface Validator {
		String validate(Object value);
	}
	
	/**
	 * Validates a field with the given validators, in order. Halts on the first validation error and sets the errors map with the field and the error message.
	 * 
	 * @param fieldName this will be used as the key on the errors map
	 * @param value the data to be validated
	 * @param validators the {@code Validator}s that will act upon the data
	 */
	public void validate(String fieldName, Object value, Validator... validators) {
		for (Validator v: validators) {
			String message = v.validate(value); 
			if (message != null) {
				errors.put(fieldName, message);
				return;
			}
		}
	}


	public void validate(String fieldName, Validator... validators) {
		Object value = getFieldValue(fieldName);
		this.validate(fieldName, value, validators);
	}



	private Object getFieldValue(String fieldName) {
		try {
			return extractFieldValue(fieldName);
		} catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException e) {
			return null;
		}
	}


	private Object extractFieldValue(String fieldName) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
		try {
			Method method = this.dataClazz.getMethod(fieldName)	;
			return method.invoke(this.data);
		} catch (NoSuchMethodException e) {
			Field field = this.dataClazz.getDeclaredField(fieldName);
			return field.get(this.data);
		}
	}

	
	/**
	 * Creates a {@code Validator} by using a regular expression with the matches method.
	 * 
	 * @param regex the regular expression to validate the field against
	 * @param message the error message in case validation fails
	 * @return a {@code Validator}
	 */
	public static Validator getValidatorRegexMatch(String regex, String message) {
		return (Object data) -> {
			if (data == null || ((String) data).isEmpty()) {
				return null;
			}
			Pattern pattern = Pattern.compile(regex);
			if (!pattern.matcher((String) data).matches()) {
				return message;
			}
			return null;
		};
	}
	
	/**
	 * Creates a {@code Validator} by using a regular expression with the find method.
	 * 
	 * @param regex the regular expression to validate the field against
	 * @param message the error message in case validation fails
	 * @return a {@code Validator}
	 */
	public static Validator getValidatorRegexFind(String regex, String message) {
		return (Object data) -> {
			if (data == null || ((String) data).isEmpty()) {
				return null;
			}
			Pattern pattern = Pattern.compile(regex);
			if (!pattern.matcher((String) data).find()) {
				return message;
			}
			return null;
		};
	}
}
