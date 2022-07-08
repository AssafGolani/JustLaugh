package restapi.webapp.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restapi.webapp.dto.ApiJokesDTO;
import restapi.webapp.dto.BlogDTO;
import restapi.webapp.dto.JokeDTO;
import restapi.webapp.pojos.Blog;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getJokeById(@PathVariable Long id) {
        Optional<Joke> jokeOptional = jokeRepo.findById(id);
        return jokeOptional.map(JokeDTO::new)
                .map(jokeDTOFactory::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
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
    @GetMapping("")
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

    @PostMapping("/{blogId}")
    public ResponseEntity<?> saveJokeToRepo(@RequestBody ApiJokesDTO apiJoke, @PathVariable Long blogId) {
        Joke joke = jokesService.parsedJoke(apiJoke);
        Optional<Blog> blogOptional = blogController.getBlog(blogId);
        if (blogOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: blog id is unavailable");
        }
        Optional<Joke> optionalJoke = jokeRepo.findByJoke(joke.getJoke());

        // if joke not exists at all. add to repo
        if(optionalJoke.isEmpty()) {
            Blog blog = blogOptional.get();
            joke.getBlogCollection().add(blog);
            blog.getJokeCollection().add(joke);
            jokeRepo.save(joke);
            return ResponseEntity.created(URI.create("https://localhost:8080/joke/" + joke.getId()))
                    .body(jokeDTOFactory.toModel(new JokeDTO(joke)));
        }
        // if joke exists already. add her to desired blog.
        else{
            Joke joke1 = optionalJoke.get();
            Blog blog1 = blogOptional.get();
            if(!joke1.getBlogCollection().contains(blog1)){
                joke1.getBlogCollection().add(blog1);
                blog1.getJokeCollection().add(joke1);
                jokeRepo.save(joke1);
            }

            return ResponseEntity.created(URI.create("https://localhost:8080/joke/" + joke1.getId()))
                    .body(jokeDTOFactory.toModel(new JokeDTO(joke1)));
        }
    }

    @DeleteMapping("/delete/{blogId}/{jokeId}")
    public ResponseEntity<?> deleteJoke(@PathVariable Long blogId, @PathVariable Long jokeId) {
        Optional<Joke> optionalJoke = jokeRepo.findById(jokeId);
        Optional<Blog> optionalBlog = blogController.getBlog(blogId);

        if (optionalJoke.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: joke id is unavailable");
        } else if (optionalBlog.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: blog id is unavailable");
        }
        else {
            Blog blog = optionalBlog.get();
            boolean isRemoved = blog.getJokeCollection().removeIf(j -> j.getId().equals(jokeId));
            // if blog does not contain the given joke.
            if(!isRemoved){
                return ResponseEntity.notFound().build();
            }

            blogController.saveBlogToRepo(blog);
            Joke joke = optionalJoke.get();
            Collection<Blog> jokesInBlog = joke.getBlogCollection();
            if(jokesInBlog.size() == 1){
                jokeRepo.delete(joke);
            }
            else{
                jokesInBlog.removeIf(bId -> bId.getId().equals(blogId));
                jokeRepo.save(joke);
            }

            return ResponseEntity.ok().body(blogController.getBlogModel(blog));
        }

    }




}
