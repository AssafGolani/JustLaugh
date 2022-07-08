package restapi.webapp.exceptions.user;

import javassist.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String msg) {
        super("[USER]: " + msg);
    }
}
