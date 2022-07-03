package restapi.webapp.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restapi.webapp.dto.ApiJokesDTO;
import restapi.webapp.dto.JokeDTO;
import restapi.webapp.pojos.Blog;
import restapi.webapp.pojos.Joke;
import restapi.webapp.pojos.User;
import restapi.webapp.repos.JokeRepo;
import restapi.webapp.representationmodels.JokeDTOFactory;
import restapi.webapp.service.JokesService;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
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

    @GetMapping("/joke/{id}")
    public ResponseEntity<?> getJokeById(@PathVariable Long id) {
        Optional<Joke> jokeOptional = jokeRepo.findById(id);
        return jokeOptional.map(JokeDTO::new)
                .map(jokeDTOFactory::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/joke/all")
    public ResponseEntity<?> allJokesInfo() {
        return ResponseEntity.ok(jokeDTOFactory.toCollectionModel(
                StreamSupport.stream(jokeRepo.findAll().spliterator(),
                                false)
                        .map(JokeDTO::new)
                        .collect(Collectors.toList())));
    }

    //TODO: Add service to get new joke and parse it
    @GetMapping("/jokeFromAPI")
    public ResponseEntity<ApiJokesDTO> getNewJoke() throws ExecutionException, InterruptedException {
        CompletableFuture<ApiJokesDTO> jokeOptional = this.jokesService.joke();
        return ResponseEntity.ok(jokeOptional.get());
    }

    /**
     * get Joke By username if exists
     *
     * @param userName
     * @return get all the jokes claimed by specific user.
     */
    @GetMapping("/joke")
    public ResponseEntity<?> getJokesByUserName(@RequestParam String userName) {
        Optional<User> userOptional = userController.getUserByUserName(userName);

        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: user name not found");
        }

        User user = userOptional.get();
        List<JokeDTO> jokes = user.getStringBlogMap().values().stream()
                .map(Blog::getJokeCollection)
                .flatMap(List::stream)
                .map(JokeDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                jokeDTOFactory.toCollectionModel(jokes));
    }

    @PostMapping("/joke/{blogId}")
    public ResponseEntity<?> saveJokeToRepo(@RequestBody ApiJokesDTO apiJoke, @PathVariable Long blogId) {
        Joke joke = jokesService.parsedJoke(apiJoke);
        Optional<Blog> blogOptional = blogController.getBlog(blogId);
        if (blogOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: blog id is unavailable");
        }
        Blog blog = blogOptional.get();
        joke.getBlogCollection().add(blog);
        blog.getJokeCollection().add(joke);
        jokeRepo.save(joke);
        return ResponseEntity.created(URI.create("https://localhost:8080/joke/" + joke.getId()))
                .body(jokeDTOFactory.toModel(new JokeDTO(joke)));
    }

    @DeleteMapping("/joke/deleteJoke")
    public ResponseEntity<?> deleteJoke(@RequestParam Long blogId, @RequestParam Long jokeId) {
        Optional<Joke> optionalJoke = jokeRepo.findById(jokeId);
        Optional<Blog> optionalBlog = blogController.getBlog(blogId);

        if (optionalJoke.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: joke id is unavailable");
        } else if (optionalBlog.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: blog id is unavailable");
        } else {
            Blog blog = optionalBlog.get();
            blog.getJokeCollection().removeIf(joke -> joke.getId().equals(jokeId));
            blogController.saveBlogToRepo(blog);
            return ResponseEntity.ok().body(blogController.getBlog(blogId));
        }
    }




}
