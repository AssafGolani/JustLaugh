package restapi.webapp.repos;

import org.springframework.data.repository.CrudRepository;
import restapi.webapp.pojos.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepo extends CrudRepository<User, Long> {
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
    Optional<List<User>> findAllByCreationDateIsBetween(LocalDate start, LocalDate end);
}
