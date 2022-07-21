package restapi.webapp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import restapi.webapp.pojos.Blog;
import restapi.webapp.pojos.Category;
import restapi.webapp.pojos.Joke;
import restapi.webapp.pojos.User;
import restapi.webapp.repos.BlogRepo;
import restapi.webapp.repos.JokeRepo;
import restapi.webapp.repos.UserRepo;

import java.time.LocalDate;
import java.util.Arrays;

@Configuration
public class SeedDB {
    private static final Logger logger = LoggerFactory.getLogger(SeedDB.class);

    @Bean
    CommandLineRunner seedDatabase(UserRepo userRepo, BlogRepo blogRepo, JokeRepo jokeRepo) {
        return args -> {
            User user = userRepo.save(new User("User1", "123@gmail.com"));
            User user2 = userRepo.save(new User("User2", "@@@@"));
            user.setCreationDate(LocalDate.of(2020, 1, 1));
            userRepo.save(user);
            Blog blog = blogRepo.save(new Blog(user, "Blog1"));
            Blog blog2 = blogRepo.save(new Blog(user2, "Blog2"));

            logger.info("logging: " + user.getStringBlogMap().keySet());

//            Joke joke = jokeRepo.save(new Joke("e=mc2", Category.A, true));
//            Joke joke1 = jokeRepo.save(new Joke("death", Category.A, true));
//            Joke joke2 = jokeRepo.save(new Joke("getting a degree", Category.A, false));

//            blog.getJokeCollection().addAll(Arrays.asList(joke,joke1));
//            blog2.getJokeCollection().addAll(Arrays.asList(joke,joke2));
//
//            joke.getBlogCollection().addAll(Arrays.asList(blog,blog2));
//            joke1.getBlogCollection().add(blog);
//            joke2.getBlogCollection().add(blog2);

//            user.getStringBlogMap().put(blog.getTitle(), blog);
//            user.getStringBlogMap().put(blog2.getTitle(), blog2);
//
//            logger.info("logging: " + user.getStringBlogMap().keySet());
//            blogRepo.save(blog);
//            blogRepo.save(blog2);
//            userRepo.save(user);
//            userRepo.save(user2);
//            jokeRepo.save(joke);
//            jokeRepo.save(joke1);
//            jokeRepo.save(joke2);
        };
    }
}
