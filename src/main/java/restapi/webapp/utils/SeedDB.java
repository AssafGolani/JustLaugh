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

        };
    }
}
