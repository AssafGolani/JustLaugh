package restapi.webapp.controllers;

import org.springframework.context.annotation.Lazy;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restapi.webapp.adapters.UserDetailsAdapter;
import restapi.webapp.dto.UserDTO;
import restapi.webapp.exceptions.blog.BlogNotFoundException;
import restapi.webapp.exceptions.user.UserAlreadyExistsException;
import restapi.webapp.exceptions.user.UserNotFoundException;
import restapi.webapp.pojos.Blog;
import restapi.webapp.pojos.User;
import restapi.webapp.repos.UserRepo;
import restapi.webapp.representationmodels.UserDTOFactory;

import java.net.URI;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
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
     * @param email of user to return
     * @return userDto if exists.
     */
    @GetMapping("/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        return userRepo.findByEmail(email)
                .map(UserDTO::new)
                .map(userDTOFactory::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * @param start date of user to return
     * @param end date of user to return
     * @return list of userDTOs
     */
    @GetMapping("/betweenDates")
    public ResponseEntity<?> getUsersBetweenDates(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        Optional<List<User>> userOptional = userRepo.findAllByCreationDateIsBetween(start, end);
        if (userOptional.isPresent()) {
            List<User> users = userOptional.get();
            return ResponseEntity.ok(
                    userDTOFactory.toCollectionModel(
                    users.stream()
                            .map(UserDTO::new)
                            .collect(Collectors.toList())));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * @param id - user Id
     * @return info about specific existing user including all owned blogs title's,
     * if user not found, return status 404 not found.
     */
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
     * @param newUserAdapter object of username and email
     * @return if newUserName is available (not in use) add a new userName with that name.
     * else return status of "bad request" and the data about the user with the given name.
     */
    @PostMapping
    public ResponseEntity<?> addNewUser(@RequestBody UserDetailsAdapter newUserAdapter){
        if(userRepo.findByName(newUserAdapter.getName()).isPresent() || userRepo.findByEmail(newUserAdapter.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("Error: User is already exist");
        }

        User userToAdd = new User(newUserAdapter.getName(), newUserAdapter.getEmail());

        userRepo.save(userToAdd);
        return ResponseEntity.created(URI.create("http://localhost:8080/users/"+userToAdd.getId() +"/info"))
                .body(userDTOFactory.toModel(new UserDTO(userToAdd)));
    }

    /**
     * @param oldUserName username to replace
     * @param newUserName new username
     * @return user model entity
     */
    @PutMapping
    public ResponseEntity<?> changeUserName(@RequestParam String oldUserName, @RequestParam String newUserName) throws UserNotFoundException {
        Optional<User> userOptional = userRepo.findByName(oldUserName);

        if(userRepo.findByName(newUserName).isPresent()){
            throw new UserAlreadyExistsException("Error: new User name already exist. Choose another name");
        }

        User user = userOptional.orElseThrow(() ->  new UserNotFoundException("Error: User name does not exist"));
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
    public ResponseEntity<?> deleteUserByUserName(@RequestParam String userName) throws UserNotFoundException, BlogNotFoundException {
        Optional<User> userOptional = userRepo.findByName(userName);

        User user = userOptional.orElseThrow(() -> new UserNotFoundException("Error: User name does not exist"));
        user.getStringBlogMap().clear();
        Collection<Blog> userBlogs = user.getStringBlogMap().values();
        for (Blog blog : userBlogs) {
            blogController.deleteBlogFromUser(userName, blog.getTitle());
        }
        userRepo.delete(user);

        return ResponseEntity.ok().body(userDTOFactory.toModel(new UserDTO(user)));
    }

}
