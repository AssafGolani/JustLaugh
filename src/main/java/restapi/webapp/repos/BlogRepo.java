package restapi.webapp.repos;

import org.springframework.data.repository.CrudRepository;
import restapi.webapp.pojos.Blog;
import restapi.webapp.pojos.User;

import java.util.List;

public interface BlogRepo extends CrudRepository<Blog, Long> {
    List<Blog> findByTitle(String title);
    List<Blog> findByCreator(User creator);
}
