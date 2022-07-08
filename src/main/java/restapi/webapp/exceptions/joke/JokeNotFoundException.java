package restapi.webapp.exceptions.joke;

import javassist.NotFoundException;

public class JokeNotFoundException extends NotFoundException {
    public JokeNotFoundException(String msg) {
        super("[JOKE]: " + msg);
    }
}
