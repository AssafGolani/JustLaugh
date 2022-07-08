package restapi.webapp.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import restapi.webapp.exceptions.blog.BlogAlreadyExistsException;
import restapi.webapp.exceptions.blog.BlogNotFoundException;

@ControllerAdvice
public class BlogExceptionAdvice {

    @ExceptionHandler(BlogNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String BlogNotFoundHandler(BlogNotFoundException blogNotFoundException){
        return blogNotFoundException.getMessage();
    }

    @ExceptionHandler(BlogAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String BlogAlreadyExistsHandler(BlogAlreadyExistsException blogAlreadyExistsException){
        return blogAlreadyExistsException.getMessage();
    }
}
