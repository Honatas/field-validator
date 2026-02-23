package com.github.honatas.fieldvalidator;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.github.honatas.fieldvalidator.exception.FieldValidationException;

/**
 * <p>Provides means for registering validators and validate data with them.</p>
 * 
 * @author Jonatas "Honatas" de Moraes Junior
 */
public class FieldValidator {

	private Logger logger = Logger.getLogger(FieldValidator.class.getName());

	/** The errors map. */
	protected Map<String, String> errors = new HashMap<>();

	/** The data to be validated. */
	protected Serializable data;

	/** The class of the data to be validated. */
	protected Class<?> dataClazz;

	/** Creates a {@code FieldValidator} without data. */
	public FieldValidator() {
		this.logger.fine("Validator created without data");
	}

	/** 
	 * Creates a {@code FieldValidator} with data. 
	 * @param data the data to be validated
	 */
	public FieldValidator(Serializable data) {
		this.data = data;
		this.dataClazz = data.getClass();
		this.logger.fine("Validator created with data from class " + this.dataClazz.getName());
	}
	
	/** 
	 * Returns the errors map.
	 * @return A {@code java.util.Map} conatining an error message for each field. 
	 */
	public Map<String, String> getErrors() {
		return this.errors;
	}

	/** 
	 * Returns the data that has been passed to the constructor.
	 * @return The data that is being validated. 
	 */
	public Serializable getData() {
		return this.data;
	}
	
	/** 
	 * Returns true if a validation error has occurred, false otherwise.
	 * @return true if a validation error has occurred, false otherwise 
	 */
	public boolean hasErrors() {
		return !this.errors.isEmpty();
	}
	
	/** 
	 * Checks if has any error and throws a {@code FieldValidationException} when errors are found.
	 * @throws FieldValidationException if there are any errors
	 */
	public void checkHasErrors() throws FieldValidationException {
		if (this.hasErrors()) {
			throw new FieldValidationException(this.errors, this.data);
		}
	}
	
	/** 
	 * Returns true if there is an error for the specified field.
	 * @param field the field to check
	 * @return true if there is an error for the specified field. */
	public boolean hasErrorOn(String field) {
		return this.getErrors().get(field) != null;
	}
	
	/** 
	 * Explicitly adds an error on the specified field. Use it if you need FieldValidator to output the result of external validations.
	 * @param field the field to add the error to
	 * @param error the error message
	 */
	public void addErrorOn(String field, String error) {
		this.getErrors().put(field, error);
	}

	/** 
	 * Returns the error message for the specified field, or null if there is no error.
	 * @param field the field to get the error for
	 * @return The error message for the specified field, or null if there is no error.
	 */
	public String getErrorOn(String field) {
		return this.getErrors().get(field);
	}
	
	/**
	 * Validator functional interface, represents a validation method.
	 */
	@FunctionalInterface
	public interface Validator {
		/**
		 * Validates the given value.
		 * @param value the value to validate
		 * @param fieldName the name of the field being validated
		 * @return the error message, or null if there is no error
		 */
		String validate(Object value, String fieldName);
	}
	
	/**
	 * Validates a field with the given validators, in order. Halts on the first validation error and sets the errors map with the field and the error message.
	 * Use this method if you didn't pass an object to the constructor.
	 * 
	 * @param value the data to be validated
	 * @param fieldName this will be used as the key on the errors map
	 * @param validators the {@code Validator}s that will act upon the data
	 */
	public void validate(Object value, String fieldName, Validator... validators) {
		if (validators == null || validators.length == 0) {
			throw new IllegalArgumentException("You need to pass at least one validator");
		}
		if (fieldName == null || fieldName.isEmpty()) {
			throw new IllegalArgumentException("You need to pass a field name");
		}
		this.logger.log(Level.FINE, "Validating field {0} with data {1}", new Object[] {fieldName, value});
		for (Validator v: validators) {
			String message = v.validate(value, fieldName);
			this.logger.log(Level.FINE, "Validation passed");
			if (message != null) {
				this.logger.log(Level.FINE, "Validation failed");
				errors.put(fieldName, message);
				return;
			}
		}
	}

	/**
	 * Validates a field with the given validators, in order. Halts on the first validation error and sets the errors map with the field and the error message.
	 * Use this method if you passed an object to the constructor.
	 * 
	 * @param fieldName name of the field in the object. This will also be used as the key on the errors map
	 * @param validators the {@code Validator}s that will act upon the data
	 */
	public void validate(String fieldName, Validator... validators) {
		if (this.data == null) {
			throw new IllegalStateException("You need to pass an object to be validated at the constructor if you want to use FieldValidator::validate without passing the value");
		}
		Object value = getFieldValue(fieldName);
		this.validate(value, fieldName, validators);
	}

	private Object getFieldValue(String fieldName) {
		try {
			return extractFieldValue(fieldName);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			return null;
		}
	}

	private Object extractFieldValue(String fieldName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException {
		try {
			String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			Method getter = this.dataClazz.getMethod(getterName);
			return getter.invoke(this.data);
		} catch (NoSuchMethodException e) {
			Method accessor = this.dataClazz.getMethod(fieldName);
			return accessor.invoke(this.data);
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
		return (Object data, String fieldName) -> {
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
		return (Object data, String fieldName) -> {
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
