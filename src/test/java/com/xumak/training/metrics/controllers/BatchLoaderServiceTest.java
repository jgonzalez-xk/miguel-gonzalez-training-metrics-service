package com.xumak.training.metrics.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import com.xumak.training.metrics.data.BatchLoaderRepository;
import com.xumak.training.metrics.models.BatchLoader;
import com.xumak.training.metrics.services.impl.BatchLoaderServiceImpl;
import com.xumak.training.metrics.utils.BatchLoaderModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@ExtendWith(MockitoExtension.class)
public class BatchLoaderServiceTest {

        @InjectMocks
        BatchLoaderServiceImpl service;
        @Mock
        BatchLoaderRepository repo;
        @Mock
        BatchLoaderModelAssembler assembler;

        @Test
        void findById() {
                // Mock BatchLoader Object
                BatchLoader personResolution = new BatchLoader("NewFile.txt");

                // Mock BatchLoaderRepository
                Mockito.when(repo.findById(personResolution.getId()))
                                .thenReturn(Optional.of(personResolution));

                // Mock BatchLoaderModelAssembler
                EntityModel<BatchLoader> entityModel = EntityModel.of(personResolution,
                                linkTo(methodOn(BatchLoaderController.class).one(personResolution.getId()))
                                                .withSelfRel());
                Mockito.when(assembler.toModel(personResolution)).thenReturn(entityModel);

                // Assert BatchLoaderServiceImpl return the correct EntityModel
                assertEquals(service.findById(personResolution.getId()), entityModel);
        }

        @Test
        void findBetweenDates() {

                List<BatchLoader> newList = new ArrayList<>();
                BatchLoader personResolution = new BatchLoader("NewFile.txt");
                newList.add(personResolution);

                EntityModel<BatchLoader> entityModel = EntityModel.of(personResolution,
                                linkTo(methodOn(BatchLoaderController.class).one(personResolution.getId()))
                                                .withSelfRel());

                Date start_date = Date.from(ZonedDateTime.now().minusMonths(5).toInstant());
                Date end_date = new Date();
                List<EntityModel<BatchLoader>> personResolutions = new ArrayList<>();
                personResolutions.add(entityModel);
                CollectionModel<EntityModel<BatchLoader>> collectionModel = CollectionModel.of(personResolutions,
                                linkTo(methodOn(BatchLoaderController.class).all(start_date,
                                                end_date)).withSelfRel());
                Mockito.when(repo.findByCreatedAtBetween(start_date, end_date)).thenReturn(newList);
                Mockito.when(assembler.toModel(personResolution)).thenReturn(entityModel);

                assertEquals(service.findBetweenDates(start_date, end_date), collectionModel);
        }

}
