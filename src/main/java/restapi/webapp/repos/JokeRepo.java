package restapi.webapp.repos;

import org.springframework.data.repository.CrudRepository;
import restapi.webapp.pojos.Joke;
import restapi.webapp.pojos.User;

import java.util.List;
import java.util.Optional;

public interface JokeRepo extends CrudRepository<Joke, Long> {
    Optional<Joke> findByJoke(String string);
}
