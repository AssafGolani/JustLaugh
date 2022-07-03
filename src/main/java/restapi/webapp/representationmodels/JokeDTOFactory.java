package restapi.webapp.representationmodels;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;
import restapi.webapp.controllers.BlogController;
import restapi.webapp.controllers.JokeController;
import restapi.webapp.controllers.UserController;
import restapi.webapp.dto.JokeDTO;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class JokeDTOFactory implements SimpleRepresentationModelAssembler<JokeDTO> {
    @Override
    public void addLinks(EntityModel<JokeDTO> resource) {
        resource.add(
                linkTo(methodOn(JokeController.class).getJokeById(Objects.requireNonNull(resource.getContent()).getId()))
                        .withSelfRel());

//        resource.add(linkTo(methodOn(BlogController.class).blogsInfo(resource.getContent().getBlogs()))
//                .withRel("Blogs contained information"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<JokeDTO>> resources) {
        resources.add(linkTo(methodOn(JokeController.class).allJokesInfo()).withSelfRel());

    }
}
