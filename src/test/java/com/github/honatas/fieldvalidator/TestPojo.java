package com.github.honatas.fieldvalidator;

import java.io.Serializable;

public class TestPojo implements Serializable {

    private String name;


    public TestPojo() {
    }

    public TestPojo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
