
### Usage

- Add dependency

```xml

<dependency>
    <groupId>spring.error-handler</groupId>
    <artifactId>handler</artifactId>
    <version>0.1.0</version>
</dependency>
```
- Enable it (Optional)
```java
@SpringBootApplication
-> @EnableErrorHandling
public class Application {
	public static void main(String[] args) { SpringApplication.run(Application.class, args); }
}
```

- Define your error http status, error message and error code via annotation.
``` 
@ErrorResponse(code = "123", message = "api.error.USER_NOT_FOUND_EXCEPTION.message", httpStatus = 404)
public class UserNotFoundException extends RuntimeException {
}
```
will output
``` 
HTTP 404
{
    "code": "123",
    "message": "api.error.USER_NOT_FOUND_EXCEPTION.message" //configurable via message bundle
}
```

- Override your error response by marking any method with _*@ErrorBody*_ in your exception class.
``` 
@ErrorResponse(httpStatus = 404)
public class UserNotFoundException extends RuntimeException {
  
  @ErrorBody
  public Object errorBody(){  // should have zero input params 
     return Map.of(
         "field1",  "message",
         "field2", 123 
     );
  }
}
```
will output
```
HTTP 404
{
  "field1": "message",
  "field2": 123
}
```
- Validation errors are handled automatically, example validation response:
``` 
{
    "message": "Bad request, please check your input.",
    "errors": [
        {
            "field": "orderDate",
            "message": "Please select a valid date of order."
        },
        {
            "field": "test",
            "message": "api.validation.constraints.TEST.Pattern.OnlyDigit.message"
        }
    ]
}
```

- Invalid JSON requests are also handled, for example: 
``` 
// Request. 
{
    "status": "INVALID_ENUM_VALUE"  // Valid values: ACTIVE, PASSIVE
    "integerField":  "AAA"
}

// Error Response
{
    "message": "Bad request, please check your input.",
    "errors": [
        {
            "field": "status",
            "message": "api.error.invalid.status.message"
        },
        {
            "field": "integerField",
            "message": "api.error.invalid.integerField.message"
        }
    ]
}
```  

- Override default messages if you want
``` 
api.error.unknown.error.message = Something went wrong. Please try again.
api.error.invalid.request.message = Bad request, please check your input.
```

- By default, HTTP headers are logged in case of an exception. You can disable it by setting 
``` 
error-handler.log-request-headers = false
```