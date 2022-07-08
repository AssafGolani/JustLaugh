package restapi.webapp.exceptions.blog;

import javassist.NotFoundException;

public class BlogNotFoundException extends NotFoundException {
    public BlogNotFoundException(String msg) {
        super("[BLOG]: " + msg);
    }
}
