package restapi.webapp.pojos;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@NoArgsConstructor
public class User {
    @GeneratedValue @Id
    private Long id;
    private String name;

    @OneToMany(mappedBy = "creator") @MapKey(name = "title")
    private Map<String, Blog> stringBlogMap = new HashMap<>();

    public User(String name) {
        this.name = name;
    }
}
