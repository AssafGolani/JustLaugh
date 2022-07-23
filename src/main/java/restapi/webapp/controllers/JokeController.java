package restapi.webapp.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restapi.webapp.dto.ApiJokesDTO;
import restapi.webapp.dto.BlogDTO;
import restapi.webapp.dto.JokeDTO;
import restapi.webapp.exceptions.blog.BlogNotFoundException;
import restapi.webapp.exceptions.joke.JokeNotFoundException;
import restapi.webapp.exceptions.user.UserNotFoundException;
import restapi.webapp.pojos.Blog;
import restapi.webapp.pojos.Category;
import restapi.webapp.pojos.Joke;
import restapi.webapp.pojos.User;
import restapi.webapp.repos.JokeRepo;
import restapi.webapp.representationmodels.BlogDTOFactory;
import restapi.webapp.representationmodels.JokeDTOFactory;
import restapi.webapp.service.JokesService;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/jokes")
public class JokeController {
    private final JokeRepo jokeRepo;
    private final JokeDTOFactory jokeDTOFactory;
    private final UserController userController;
    private final BlogController blogController;
    private final JokesService jokesService;

    public JokeController(JokeRepo jokeRepo, JokeDTOFactory jokeDTOFactory, UserController userController, @Lazy BlogController blogController, JokesService jokesService) {
        this.jokeRepo = jokeRepo;
        this.jokeDTOFactory = jokeDTOFactory;
        this.userController = userController;
        this.blogController = blogController;
        this.jokesService = jokesService;
    }

    public Joke saveJokeToRepo(Joke joke) {
        return jokeRepo.save(joke);
    }

    /**
     * @param id of joke
     * @return jokeDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getJokeById(@PathVariable Long id) throws JokeNotFoundException {
        Optional<Joke> jokeOptional = jokeRepo.findById(id);
        return jokeOptional.map(JokeDTO::new)
                .map(jokeDTOFactory::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new JokeNotFoundException("Joke was not found"));
    }

    /**
     * @return collection model of all jokeDTOs
     */
    @GetMapping("/all")
    public ResponseEntity<?> allJokesInfo() {
        return ResponseEntity.ok(jokeDTOFactory.toCollectionModel(
                StreamSupport.stream(jokeRepo.findAll().spliterator(),
                                false)
                        .map(JokeDTO::new)
                        .collect(Collectors.toList())));
    }

    /**
     * @return ApiJokeDTO model
     * @throws ExecutionException   depends on outside service
     * @throws InterruptedException depends on outside service
     */
    @GetMapping("/jokeFromAPI")
    public ResponseEntity<ApiJokesDTO> getNewJoke() throws ExecutionException, InterruptedException, JokeNotFoundException {
        CompletableFuture<ApiJokesDTO> jokeOptional = this.jokesService.joke();
        if(jokeOptional.get() != null) return ResponseEntity.ok(jokeOptional.get());
        throw new JokeNotFoundException("Joke was not found");
    }

    /**
     * get Joke By username if exists
     *
     * @param userName username
     * @return get all the jokes claimed by specific user.
     */
    @GetMapping("")
    public ResponseEntity<?> getJokesByUserName(@RequestParam String userName) throws UserNotFoundException {
        Optional<User> userOptional = userController.getUserByUserName(userName);

        User user = userOptional.orElseThrow(() -> new UserNotFoundException("Error: User was not found"));
        List<JokeDTO> jokes = user.getStringBlogMap().values().stream()
                .map(Blog::getJokeCollection)
                .flatMap(List::stream)
                .map(JokeDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                jokeDTOFactory.toCollectionModel(jokes));
    }

    /**
     * get Joke by category
     * @param category
     * @return get all the jokes in specific category.
     */
    @GetMapping("/category")
    public ResponseEntity<?> getJokesByCategory(@RequestParam Category category) {
        Optional<List<Joke>> optionalJokes = jokeRepo.findByCategory(category);
        if (optionalJokes.isPresent()) {
            List<Joke> jokes = optionalJokes.get();
            return ResponseEntity.ok(jokeDTOFactory.toCollectionModel(jokes.stream().map(JokeDTO::new).collect(Collectors.toList())));
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * @param apiJoke joke as received from getJoke
     * @param blogId  blog if whom you want to add the joke to.
     * @return blogDTO after adding the joke to blog
     */
    @PostMapping("/{blogId}")
    public ResponseEntity<?> saveJokeToRepo(@RequestBody ApiJokesDTO apiJoke, @PathVariable Long blogId) throws BlogNotFoundException {
        Joke joke = jokesService.parsedJoke(apiJoke);
        Optional<Blog> blogOptional = blogController.getBlog(blogId);

        Optional<Joke> optionalJoke = jokeRepo.findByJoke(joke.getJoke());

        // if joke not exists at all. add to repo
        if (optionalJoke.isEmpty()) {
            Blog blog = blogOptional.orElseThrow(() -> new BlogNotFoundException("Error: Blog was not found"));
            addJokeToBlog(joke, blog);
            return ResponseEntity.created(URI.create("http://localhost:8080/jokes/" + joke.getId()))
                    .body(jokeDTOFactory.toModel(new JokeDTO(joke)));
        }
        // if joke exists already. add her to desired blog.
        else {
            Joke joke1 = optionalJoke.get();
            Blog blog1 = blogOptional.orElseThrow(() -> new BlogNotFoundException("Error: Blog was not found"));
            if (!joke1.getBlogCollection().contains(blog1)) {
                addJokeToBlog(joke1, blog1);
            }

            return ResponseEntity.created(URI.create("http://localhost:8080/jokes/" + joke1.getId()))
                    .body(jokeDTOFactory.toModel(new JokeDTO(joke1)));
        }
    }

    private void addJokeToBlog(Joke joke, Blog blog) {
        joke.getBlogCollection().add(blog);
        blog.getJokeCollection().add(joke);
        jokeRepo.save(joke);
    }


    /**
     * @param blogId blog which we would like to remove the joke from
     * @param jokeId joke id whom you want to remove
     * @return BlogDTO after the removal.
     */
    @DeleteMapping("/delete/{blogId}/{jokeId}")
    public ResponseEntity<?> deleteJoke(@PathVariable Long blogId, @PathVariable Long jokeId) throws Throwable {
        Optional<Joke> optionalJoke = jokeRepo.findById(jokeId);
        Optional<Blog> optionalBlog = blogController.getBlog(blogId);

        Blog blog = optionalBlog.orElseThrow(() -> new BlogNotFoundException("Error: blog id is unavailable"));
        boolean isRemoved = blog.getJokeCollection().removeIf(j -> j.getId().equals(jokeId));
        // if blog does not contain the given joke.
        if (!isRemoved) {
            return ResponseEntity.notFound().build();
        }

        blogController.saveBlogToRepo(blog);
        Joke joke = optionalJoke.orElseThrow(() -> new JokeNotFoundException("Error: joke id is unavailable"));
        Collection<Blog> jokesInBlog = joke.getBlogCollection();
        if (jokesInBlog.size() == 1) {
            jokeRepo.delete(joke);
        } else {
            jokesInBlog.removeIf(bId -> bId.getId().equals(blogId));
            jokeRepo.save(joke);
        }

        return ResponseEntity.ok().body(blogController.getBlogModel(blog));
    }
}
