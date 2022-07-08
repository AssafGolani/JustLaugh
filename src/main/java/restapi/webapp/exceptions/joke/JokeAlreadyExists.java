package restapi.webapp.exceptions.joke;

public class JokeAlreadyExists extends RuntimeException{
    public JokeAlreadyExists(String message) {
        super("[JOKE]: " + message);
    }
}
