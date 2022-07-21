package restapi.webapp.pojos;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@NoArgsConstructor
public class User {
    @GeneratedValue @Id
    private Long id;
    private String name;

    private String email;

    private LocalDate creationDate = LocalDate.now();

    @OneToMany(mappedBy = "creator") @MapKey(name = "title")
    private Map<String, Blog> stringBlogMap = new HashMap<>();

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
