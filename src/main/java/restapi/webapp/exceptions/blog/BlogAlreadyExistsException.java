package restapi.webapp.exceptions.blog;

public class BlogAlreadyExistsException extends RuntimeException{
    public BlogAlreadyExistsException(String message) {
        super("[BLOG]: " + message);
    }
}
