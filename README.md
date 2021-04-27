# Field Validator

[![GitHub](https://img.shields.io/github/license/honatas/field-validator?style=plastic)](https://github.com/Honatas/field-validator "View this project on GitHub")
[![Maven Central](https://img.shields.io/maven-central/v/com.github.honatas/field-validator.svg?style=plastic)](https://search.maven.org/search?q=g:%22com.github.honatas%22%20AND%20a:%22field-validator%22)
[![coffee](https://img.shields.io/badge/buy%20me%20a-coffee-brown?style=plastic)](https://ko-fi.com/honatas "Buy me a coffee")  


An imperative data validator written with java 8's lambda.  

## Download

If you are using Maven, add this to your pom.xml in the dependencies section:

```xml
    <dependency>
        <groupId>com.github.honatas</groupId>
        <artifactId>field-validator</artifactId>
        <version>0.0.2</version>
    </dependency>
```

## Usage

At first, inherit FieldValidator class and create your Validator functions:

```java
import com.github.honatas.fieldvalidator.FieldValidator;

public class MyFieldValidator extends FieldValidator {

    public Validator required = (Object field) -> {
        if (field == null || (field instanceof String && ((String) field).isEmpty()) || (field instanceof Number && ((Number) field).intValue() == 0)) {
            return "Value is required";
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

Then you can create a new instance of your FieldValidator to check on your data:

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

## How it works

The Validator functions have to be written in a way where they either return null if the data is valid, or a string message detailing why the validation has failed.  

You can pass as many Validators as you want to the validate method. FieldValidator will run each one of them in the order they were passed, and will halt on the first Validator that does not return null, adding the error message to the errors Map using the fieldName as key.  

## Error checking

There are two ways you can check for errors after running your validators. You can either use the **FieldValidator::hasErrors** method which returns a boolean stating if any validation error has occurred, or you can use the **FieldValidator::checkHasErrors** method which instead of returning anything, throws a **FieldValidationException** if any validation error has occurred. This exception class has a **getErrors()** method that returns the errors Map.  

## Parameterized Validations

You can create validators with parameters, here is an example:

```java
public class MyFieldValidator extends FieldValidator {

	public Validator maxLength(int size) {
    	return (value) -> {
    		if (value == null) {
    			return null;
    		}
    		if (value.toString().length() > size) {
    			return "Field must have maximum " + size + " characters";
    		}
    		return null;
    	};
    }
}
```

And then you call the validator like this:

```java
    MyFieldValidator v = new MyFieldValidator();
    v.validate("field01", "abcde", v.maxLength(2));
```


## Regex Validation

You can create regex Validators by using the static methods **FieldValidator::getValidatorRegexMatch** and **FieldValidator::getValidatorRegexFind**:  

```java
public class MyFieldValidator extends FieldValidator {

    public Validator number = FieldValidator.getValidatorRegexMatch("\\d+", "Value has to be a number");
}
```

## Contributions

Feel free to open an issue or add a pull request. Anytime. Really, I mean it.  

Also, if you like my work, I'll let you know that I love [coffee](https://ko-fi.com/honatas).
