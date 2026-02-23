package com.github.honatas.fieldvalidator;

import java.io.Serializable;

public class TestValidator extends FieldValidator {

    public TestValidator() {
    }

    public TestValidator(Serializable data) {
        super(data);
    }

	public Validator required = (Object field) -> {
		if (field == null || (field instanceof String string && string.isEmpty()) || (field instanceof Number number && number.intValue() == 0)) {
			return "Value is required";
		}
		return null;
	};

}
