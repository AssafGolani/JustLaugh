package restapi.webapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;
import restapi.webapp.pojos.User;

import java.time.LocalDate;
import java.util.Set;

@Value
@JsonPropertyOrder({"id","name","keys"})
public class UserDTO {
    @JsonIgnore
    private final User user;

    public Long getId() {return this.user.getId();}

    public String getName() {return this.user.getName();}

    public Set<String> getKeys() {return this.user.getStringBlogMap().keySet();}

    public LocalDate getCreationDate() {return this.user.getCreationDate();}

    public String getEmail(){return this.user.getEmail();}
}
