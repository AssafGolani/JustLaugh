package restapi.webapp.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import restapi.webapp.exceptions.joke.JokeAlreadyExists;
import restapi.webapp.exceptions.joke.JokeNotFoundException;

@ControllerAdvice
public class JokeExceptionAdvice {
    @ExceptionHandler(JokeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String JokeNotFoundExceptionHandler(JokeNotFoundException jokeNotFoundException){
        return jokeNotFoundException.getMessage();
    }

    @ExceptionHandler(JokeAlreadyExists.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String JokeAlreadyExistsHandler(JokeAlreadyExists jokeAlreadyExists){
        return jokeAlreadyExists.getMessage();
    }

}
