package restapi.webapp.pojos;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Entity
@NoArgsConstructor
public class Joke {
    @GeneratedValue @Id
    private Long id;
    private Category category;
    private String joke;
    private boolean isNSFW; // Not Suitable For Work

    @ManyToMany
    private Collection<Blog> blogCollection = new ArrayList<>(); // blogs which contains curtain joke

    public Joke(String joke, Category category, boolean isNSFW) {
//        this.author = author;
        this.category = category;
        this.isNSFW = isNSFW;
        this.joke = joke;
    }
}
