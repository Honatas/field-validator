package com.github.honatas.fieldvalidator;

import java.io.Serializable;

public record TestRecord(String name, Integer age, Integer height) implements Serializable {

    public TestRecord(String name) {
        this(name, null, null);
    }
}
