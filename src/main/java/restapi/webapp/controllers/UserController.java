package restapi.webapp.controllers;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.context.annotation.Lazy;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restapi.webapp.dto.JokeDTO;
import restapi.webapp.dto.UserDTO;
import restapi.webapp.pojos.Blog;
import restapi.webapp.pojos.User;
import restapi.webapp.repos.UserRepo;
import restapi.webapp.representationmodels.UserDTOFactory;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class UserController {
    private final UserRepo userRepo;
    private final UserDTOFactory userDTOFactory;
    private final BlogController blogController;

    public UserController(UserRepo userRepo, UserDTOFactory userDTOFactory,@Lazy BlogController blogController) {
        this.userRepo = userRepo;
        this.userDTOFactory = userDTOFactory;
        this.blogController = blogController;
    }

    public Optional<User> getUserByUserName(String userName){
        return userRepo.findByName(userName);
    }

    public User saveUserToRepo(User user){
        return userRepo.save(user);
    }

    public EntityModel<UserDTO> getUserModel(User user){
        return userDTOFactory.toModel(new UserDTO(user));
    }

    /**
     *
     * @param id - user Id
     * @return info about specific existing user including all owned blogs title's,
     * if user not found, return status 404 not found.
     */
    //TODO: change to search by userName?.
    //TODO: add link to blogs (?).
    @GetMapping("/users/{id}/info")
    public ResponseEntity<?> userInfo(@PathVariable Long id){
        return userRepo.findById(id)
                .map(UserDTO::new)
                .map(userDTOFactory::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     *
     * @return info about all users
     */
    @GetMapping("/users/allUsers")
    public ResponseEntity<CollectionModel<EntityModel<UserDTO>>> allUsersInfo(){
        return ResponseEntity.ok(
                userDTOFactory.toCollectionModel(
                        StreamSupport.stream(userRepo.findAll().spliterator(),
                                false)
                                .map(UserDTO::new)
                                .collect(Collectors.toList())));
    }

    /**
     *
     * @param userName who owned the jokes
     * @param blogTitle which we want to get the jokes from
     * @return a list of all Jokes in curtain blog
     *
     * //TODO: chance signature to <?> in case userName/BlogTitle do not exist.
     */
//    @GetMapping("/{userName}/{blogTitle}")
//    public ResponseEntity<CollectionModel<EntityModel<JokeDTO>>>
//    getJokesFromBlog(@PathVariable String userName, @PathVariable String blogTitle){
//        throw new NotImplementedException(); // TODO: use method on function and callback to other controllers
//    }

    /**
     * generate random joke from service
     * @param userName which defines which user wants it in-case of willing to save joke to user's blog
     * @param blogTitle defines which blog will the user save the proposed joke if decided.
     * @return generated joke with:
     * 1. link to save to the blog under the given userName and blogTitle provided.
     * //TODO: add JokeDTOFactory with this method.
     * //TODO: check userName, BlogTitle validity. (and change signature to <?>)
     * 2. link to generate new joke (again).
     */
    @GetMapping("/{userName}/{blogTitle}/generateJoke")
    public ResponseEntity<EntityModel<JokeDTO>>
    generateJoke(@PathVariable String userName,@PathVariable String blogTitle){
        throw new NotImplementedException();
    }


    /**
     * add a new user if given userName doesn't exist.
     * @param newUserName
     * @return if newUserName is available (not in use) add a new userName with that name.
     * // TODO: change else's description if changed to and exception or such.
     * else return status of "bad request" and the data about the user with the given name.
     */
    @PostMapping("/users/{newUserName}")
    public ResponseEntity<?> addNewUser(@PathVariable String newUserName){
        Optional<User> user = userRepo.findByName(newUserName);
        if(user.isPresent()){
            //TODO: throw and catch exception saying "userName already exist".
            return ResponseEntity.badRequest().body(userDTOFactory.toModel(user.map(UserDTO::new).get()));
        }

        User userToAdd = new User(newUserName);

        userRepo.save(userToAdd);
        return ResponseEntity.created(URI.create("http://localhost:8080/"+newUserName))
                .body(userDTOFactory.toModel(new UserDTO(userToAdd)));
    }

    /**
     *
     * @param oldUserName
     * @param newUserName
     * @return user model entity
     */
    @PutMapping("/users")
    public ResponseEntity<?> changeUserName(@RequestParam String oldUserName, @RequestParam String newUserName){
        Optional<User> userOptional = userRepo.findByName(oldUserName);

        // is oldUserName exists
        if(userOptional.isEmpty()){
            ResponseEntity.badRequest().body("Error: User name does not exist");
        }

        // is newUserName available
        if(userRepo.findByName(newUserName).isPresent()){
            ResponseEntity.badRequest().body("Error: new User name already exist. Choose another name");
        }


        User user = userOptional.get();
        user.setName(newUserName);
        userRepo.save(user);

        return ResponseEntity.ok().body(userDTOFactory.toModel(new UserDTO(user)));

    }

    /**
     * Delete user by UserName
     * @param userName
     * @return
     */
    @DeleteMapping("/users")
    public ResponseEntity<?> deleteUserByUserName(@RequestParam String userName){
        Optional<User> userOptional = userRepo.findByName(userName);

        // is userName exists
        if(userOptional.isEmpty()){
            ResponseEntity.badRequest().body("Error: User name does not exist");
        }

        User user = userOptional.get();
        user.getStringBlogMap().clear();
        Collection<Blog> userBlogs = user.getStringBlogMap().values();
        for (Blog blog : userBlogs) {
            blogController.deleteBlogFromUser(userName, blog.getTitle());
        }
        userRepo.delete(user);

        return ResponseEntity.ok().body(userDTOFactory.toModel(new UserDTO(user)));
    }

}
