package com.xumak.training.metrics.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import com.xumak.training.metrics.controllers.BatchLoaderController;
import com.xumak.training.metrics.data.BatchLoaderRepository;
import com.xumak.training.metrics.models.BatchLoader;
import com.xumak.training.metrics.services.impl.BatchLoaderServiceImpl;
import com.xumak.training.metrics.utils.BatchLoaderModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@ExtendWith(MockitoExtension.class)
public class BatchLoaderServiceTest {

        // MOCK VARIABLES
        BatchLoader batchLoader;
        EntityModel<BatchLoader> entityModel;

        @InjectMocks
        BatchLoaderServiceImpl service;
        @Mock
        BatchLoaderRepository repo;
        @Mock
        BatchLoaderModelAssembler assembler;

        @BeforeEach
        void setup() {
                // Mock BatchLoader Object
                batchLoader = new BatchLoader("NewFile.txt");
                // Mock BatchLoaderModelAssembler
                entityModel = EntityModel.of(batchLoader,
                                linkTo(methodOn(BatchLoaderController.class).one(batchLoader.getId()))
                                                .withSelfRel());
                // Mock BatchLoaderAssempler
                Mockito.when(assembler.toModel(batchLoader)).thenReturn(entityModel);
        }

        @Test
        void findById() {
                // Mock BatchLoaderRepository findById
                Mockito.when(repo.findById(batchLoader.getId()))
                                .thenReturn(Optional.of(batchLoader));

                assertEquals(service.findById(batchLoader.getId()), entityModel);
        }

        @Test
        void findBetweenDates() {
                Date start_date = Date.from(ZonedDateTime.now().minusMonths(5).toInstant());
                Date end_date = new Date();

                // List for the mocked repo
                List<BatchLoader> newList = new ArrayList<>();
                newList.add(batchLoader);

                // List for the expected result
                List<EntityModel<BatchLoader>> batchLoaders = new ArrayList<>();
                batchLoaders.add(entityModel);

                // Expected result
                CollectionModel<EntityModel<BatchLoader>> collectionModel = CollectionModel.of(batchLoaders,
                                linkTo(methodOn(BatchLoaderController.class).all(start_date,
                                                end_date)).withSelfRel());

                // Mock BatchLoaderRepository findByCreatedAtBetween
                Mockito.when(repo.findByCreatedAtBetween(start_date, end_date)).thenReturn(newList);

                assertEquals(service.findBetweenDates(start_date, end_date), collectionModel);
        }

}
