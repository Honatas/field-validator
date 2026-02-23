# Field Validator

[![GitHub](https://img.shields.io/github/license/honatas/field-validator?style=plastic)](https://github.com/Honatas/field-validator "View this project on GitHub")
[![Maven Central](https://img.shields.io/maven-central/v/com.github.honatas/field-validator.svg?style=plastic)](https://search.maven.org/search?q=g:%22com.github.honatas%22%20AND%20a:%22field-validator%22)
[![coffee](https://img.shields.io/badge/buy%20me%20a-coffee-brown?style=plastic)](https://ko-fi.com/honatas "Buy me a coffee")  


An imperative, zero-dependency data validator.  

## Download

If you are using Maven, add this to your pom.xml in the dependencies section:

```xml
    <dependency>
        <groupId>com.github.honatas</groupId>
        <artifactId>field-validator</artifactId>
        <version>1.1.0</version>
    </dependency>
```

## Usage

At first, create your own validator class by extending FieldValidator and then create your Validator functions:

```java
import com.github.honatas.fieldvalidator.FieldValidator;

public class MyFieldValidator extends FieldValidator {

    public Validator required = (Object field) -> {
        if (field == null || (field instanceof String string && string.isEmpty()) || (field instanceof Number number && number.intValue() == 0)) {
            return "Value is required";
        }
        return null;
    };

    public Validator positive = (Object field) -> {
        if (field == null || !(field instanceof Number number) || number.intValue() < 0) {
            return "Value must be greater than zero";
        }
        return null;
    };
}
```

Let's assume you have a data class to validate:

```java
public record MyData(String field01, Integer field02, Integer field03) {
}
```

Then you can create a new instance of your FieldValidator to check on your data:

```java
    MyData data = new MyData(null, 2, -3);
    MyFieldValidator v = new MyFieldValidator(data);
    v.validate("field01", v.required);
    v.validate("field02", v.required, v.positive);
    v.validate("field03", v.required, v.positive);

    System.out.println(v.getErrors());
```

This will yield the following result:

```
{field01=Value is required, field03=Value must be greater than zero}
```

## How it works

The Validator functions have to be written in a way where they either return null if the data is valid, or a string message detailing why the validation has failed.  

You can pass as many Validators as you want to the validate method. FieldValidator will run each one of them in the order they were passed, and will halt on the first Validator that does not return null, adding the error message to the errors Map using the fieldName as key.  

## Error checking

There are two ways you can check for errors after running your validators. You can either use the **FieldValidator::hasErrors** method which returns a boolean stating if any validation error has occurred, or you can use the **FieldValidator::checkHasErrors** method which instead of returning anything, throws a **FieldValidationException** if any validation error has occurred. This exception class has a **getErrors()** method that returns the errors Map and a **getData()** method that returns the data that was being validated if a data object was passed to the constructor.  

## Inline Validations

You don't need to pass a data object to the constructor. You can validate any value by passing it to the **FieldValidator::validate** method along with the field name and the validators.

```java
    MyFieldValidator v = new MyFieldValidator();
    v.validate("Joseph Climber", "name", v.required);
```

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
    v.validate("abcde", "field01", v.maxLength(2));
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

Also, if my work has helped you in any way, please consider buying me a [coffee](https://ko-fi.com/honatas).
