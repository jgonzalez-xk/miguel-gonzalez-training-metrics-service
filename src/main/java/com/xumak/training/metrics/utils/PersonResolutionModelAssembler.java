package com.xumak.training.metrics.utils;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.xumak.training.metrics.controllers.PersonResolutionController;
import com.xumak.training.metrics.models.PersonResolution;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PersonResolutionModelAssembler
        implements RepresentationModelAssembler<PersonResolution, EntityModel<PersonResolution>> {
    @Override
    public EntityModel<PersonResolution> toModel(PersonResolution personResolution) {

        return EntityModel.of(personResolution,
                linkTo(methodOn(PersonResolutionController.class).one(personResolution.getId())).withSelfRel());
    }
}
