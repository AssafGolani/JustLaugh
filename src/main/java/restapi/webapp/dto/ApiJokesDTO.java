package restapi.webapp.dto;


import lombok.Value;
import restapi.webapp.pojos.Category;

@Value
public class ApiJokesDTO {
    private final Long id;
    private final Category category;
    private final String joke;
}
