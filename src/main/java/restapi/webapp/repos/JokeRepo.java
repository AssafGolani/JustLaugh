package restapi.webapp.repos;

import org.springframework.data.repository.CrudRepository;
import restapi.webapp.pojos.Joke;
import restapi.webapp.pojos.User;

import java.util.List;

public interface JokeRepo extends CrudRepository<Joke, Long> {
}
