package com.github.honatas.fieldvalidator;

import java.io.Serializable;

public class TestValidator extends FieldValidator {

    public TestValidator() {
    }

    public TestValidator(Serializable data) {
        super(data);
    }

	public Validator required = (Object field, String fieldName) -> {
        if (field == null || (field instanceof String string && string.isEmpty()) || (field instanceof Number number && number.intValue() == 0)) {
            return fieldName + " is required";
        }
        return null;
    };

    public Validator positive = (Object field, String fieldName) -> {
        if (field == null || !(field instanceof Number number) || number.intValue() < 0) {
            return fieldName + " must be greater than zero";
        }
        return null;
    };

	public Validator maxLength(int size) {
    	return (value, fieldName) -> {
    		if (value == null) {
    			return null;
    		}
    		if (value.toString().length() > size) {
    			return fieldName + " must have maximum " + size + " characters";
    		}
    		return null;
    	};
    }
}
