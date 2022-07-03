package restapi.webapp.representationmodels;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;
import restapi.webapp.controllers.BlogController;
import restapi.webapp.controllers.UserController;
import restapi.webapp.dto.BlogDTO;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BlogDTOFactory implements SimpleRepresentationModelAssembler<BlogDTO> {
    @Override
    public void addLinks(EntityModel<BlogDTO> resource) {
        resource.add(
                linkTo(methodOn(BlogController.class).blogInfo(Objects.requireNonNull(resource.getContent()).getTitle()))
                        .withSelfRel());

        resource.add(
                linkTo(methodOn(BlogController.class).allBlogsInfo())
                .withRel("blogs information"));

        resource.add(
                linkTo(methodOn(UserController.class).userInfo(resource.getContent().getBlog().getCreator().getId()))
                .withRel("user owned information"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<BlogDTO>> resources) {
        resources.add(linkTo(methodOn(BlogController.class).allBlogsInfo()).withSelfRel());
    }
}
