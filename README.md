# Field Validator

[![GitHub](https://img.shields.io/github/license/honatas/field-validator?style=plastic)](https://github.com/Honatas/field-validator "View this project on GitHub")
[![coffee](https://img.shields.io/badge/buy%20me%20a-coffee-orange?style=plastic)](https://ko-fi.com/honatas "Buy me a coffee")  

A very simple validator using java 8's lambda.  

## Usage

At first, inherit FieldValidator class and create your Validator functions:

```java
import com.github.honatas.fieldvalidator.FieldValidator;

public class MyFieldValidator extends FieldValidator {

	public Validator required = (Object field) -> {
		if (field == null || (field instanceof String && ((String)field).isEmpty()) || (field instanceof Number && ((Number) field).intValue() == 0)) {
			return "Field is required";
		}
		return null;
	};

    public Validator positive = (Object field) -> {
		if (field == null || !(field instanceof Number) || ((Number) field).intValue() < 0) {
			return "Value must be greater than zero";
		}
		return null;
	};
}
```

After that, you can use the validator to check your data:

```java
    MyFieldValidator v = new MyFieldValidator();
    v.validate("field01", null, v.required, v.positive);
    v.validate("field02", 2, v.required, v.positive);
    v.validate("field03", -3, v.required, v.positive);
    
    System.out.println(v.getErrors());
```

This will yeld the following result:

```
{field01=Value is required, field03=Value must be greater than zero}
```



