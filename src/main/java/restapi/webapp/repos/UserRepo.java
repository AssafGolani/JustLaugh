package restapi.webapp.repos;

import org.springframework.data.repository.CrudRepository;
import restapi.webapp.pojos.User;

import java.util.Optional;

public interface UserRepo extends CrudRepository<User, Long> {
    Optional<User> findByName(String name);
}
