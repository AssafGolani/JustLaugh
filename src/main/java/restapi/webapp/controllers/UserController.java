package restapi.webapp.controllers;

import org.springframework.context.annotation.Lazy;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping("/users")
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
    @GetMapping("/{id}/info")
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
    @GetMapping("/allUsers")
    public ResponseEntity<CollectionModel<EntityModel<UserDTO>>> allUsersInfo(){
        return ResponseEntity.ok(
                userDTOFactory.toCollectionModel(
                        StreamSupport.stream(userRepo.findAll().spliterator(),
                                false)
                                .map(UserDTO::new)
                                .collect(Collectors.toList())));
    }


    /**
     * add a new user if given userName doesn't exist.
     * @param newUserName username
     * @return if newUserName is available (not in use) add a new userName with that name.
     * // TODO: change else's description if changed to and exception or such.
     * else return status of "bad request" and the data about the user with the given name.
     */
    @PostMapping("/{newUserName}")
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
     * @param oldUserName username to replace
     * @param newUserName new username
     * @return user model entity
     */
    @PutMapping
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

        if(userOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        User user = userOptional.get();
        user.setName(newUserName);
        userRepo.save(user);

        return ResponseEntity.ok().body(userDTOFactory.toModel(new UserDTO(user)));

    }

    /**
     * Delete user by UserName
     * @param userName user to delete
     * @return userDTO
     */
    @DeleteMapping
    public ResponseEntity<?> deleteUserByUserName(@RequestParam String userName){
        Optional<User> userOptional = userRepo.findByName(userName);

        // is userName exists
        if(userOptional.isEmpty()){
            ResponseEntity.badRequest().body("Error: User name does not exist");
        }

        if(userOptional.isEmpty()){
            return ResponseEntity.notFound().build();
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
