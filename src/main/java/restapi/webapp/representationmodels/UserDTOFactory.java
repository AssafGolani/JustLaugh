package restapi.webapp.representationmodels;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;
import restapi.webapp.controllers.UserController;
import restapi.webapp.dto.UserDTO;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserDTOFactory implements SimpleRepresentationModelAssembler<UserDTO> {
    @Override
    public void addLinks(EntityModel<UserDTO> resource) {
        resource.add(
                linkTo(methodOn(UserController.class).userInfo(Objects.requireNonNull(resource.getContent()).getId()))
                        .withSelfRel());

        resource.add(linkTo(methodOn(UserController.class).allUsersInfo())
                .withRel("users information"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<UserDTO>> resources) {
        resources.add(linkTo(methodOn(UserController.class).allUsersInfo()).withSelfRel());
    }
}
