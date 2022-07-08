package restapi.webapp.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import restapi.webapp.exceptions.user.UserAlreadyExistsException;
import restapi.webapp.exceptions.user.UserNotFoundException;

@RestControllerAdvice
public class UserExceptionAdvice {
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String UserAlreadyExistsExceptionHandler(UserAlreadyExistsException userAlreadyExistsException){
        return userAlreadyExistsException.getMessage();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String UserNotFoundExceptionHandler(UserNotFoundException userNotFoundException){
        return userNotFoundException.getMessage();
    }
}
