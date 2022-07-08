package restapi.webapp.exceptions.user;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message) {
        super("[USER]: " + message + "already exists");
    }
}
