package com.xumak.training.metrics.utils;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.xumak.training.metrics.controllers.BatchLoaderController;
import com.xumak.training.metrics.models.BatchLoader;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class BatchLoaderModelAssembler
        implements RepresentationModelAssembler<BatchLoader, EntityModel<BatchLoader>> {
    @Override
    public EntityModel<BatchLoader> toModel(BatchLoader batchLoader) {

        return EntityModel.of(batchLoader,
                linkTo(methodOn(BatchLoaderController.class).one(batchLoader.getId())).withSelfRel());
    }
}
