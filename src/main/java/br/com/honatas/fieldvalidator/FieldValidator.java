package br.com.honatas.fieldvalidator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import br.com.honatas.fieldvalidator.exception.FieldValidationException;

public class FieldValidator {

	private Map<String, String> errors = new HashMap<>();
	
	public Map<String, String> getErrors() {
		return this.errors;
	}
	
	public boolean hasErrors() {
		return !this.errors.isEmpty();
	}
	
	public void checkHasErrors() throws FieldValidationException {
		if (this.hasErrors()) {
			throw new FieldValidationException(this.errors);
		}
	}
	
	
	public interface Validator {
		String validate(Object data);
	}
	
	
	public void validate(String fieldName, Object data, Validator... validators) {
		for (Validator v: validators) {
			String message = v.validate(data); 
			if (message != null) {
				errors.put(fieldName, message);
				return;
			}
		}
	}
	
	
	public static Validator getValidatorRegexMatch(String regex, String message) {
		return (Object data) -> {
			if (StringUtils.isEmpty((String) data)) {
				return null;
			}
			Pattern pattern = Pattern.compile(regex);
			if (!pattern.matcher((String) data).matches()) {
				return message;
			}
			return null;
		};
	}
	
	
	public static Validator getValidatorRegexFind(String regex, String message) {
		return (Object data) -> {
			if (StringUtils.isEmpty((String) data)) {
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
