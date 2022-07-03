package restapi.webapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;
import restapi.webapp.pojos.Blog;
import restapi.webapp.pojos.Joke;

import java.util.List;
import java.util.stream.Collectors;

@Value
@JsonPropertyOrder({"id","creator","title","jokes"})
public class BlogDTO {
    @JsonIgnore
    private final Blog blog;

    public Long getId() {return this.blog.getId();}

    public String getCreator() {return this.blog.getCreator().getName();}

    public String getTitle() {return this.blog.getTitle();}

    public List<String> getJokes() {return this.blog.getJokeCollection().stream()
            .map(Joke::getJoke)
            .collect(Collectors.toList());
    }
}
