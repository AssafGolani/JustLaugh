package restapi.webapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;
import restapi.webapp.pojos.Blog;
import restapi.webapp.pojos.Joke;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Value
@JsonPropertyOrder({"id","author","category","jokeIs", "blogs"})
public class JokeDTO {
    @JsonIgnore
    private final Joke joke;

    public Long getId() {return this.joke.getId();}

    public String getCategory() {return this.joke.getCategory().toString();}

    public String getJokeIs() {return this.joke.getJoke();}

    public List<String> getBlogs(){ return
            this.joke.getBlogCollection().stream()
                    .map(blog -> {return blog.getTitle() + " By: " + blog.getCreator().getName();})
                    .collect(Collectors.toList());}
}
