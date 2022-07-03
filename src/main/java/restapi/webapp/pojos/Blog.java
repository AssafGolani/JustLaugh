package restapi.webapp.pojos;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Blog {
    @GeneratedValue @Id
    private Long id;

    @JsonIgnore @ManyToOne
    private User creator;

    @ManyToMany
    private List<Joke> jokeCollection = new ArrayList<>();

    private String title;

    public Blog(User creator, String title) {
        this.creator = creator;
        this.title = title;
    }
}