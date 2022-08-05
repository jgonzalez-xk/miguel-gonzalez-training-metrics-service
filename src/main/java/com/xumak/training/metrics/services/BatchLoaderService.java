package com.xumak.training.metrics.services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.xumak.training.metrics.controllers.BatchLoaderController;
import com.xumak.training.metrics.data.BatchLoaderRepository;
import com.xumak.training.metrics.models.BatchLoader;
import com.xumak.training.metrics.utils.BatchLoaderModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BatchLoaderService {
    private final BatchLoaderRepository repository;
    private final BatchLoaderModelAssembler assembler;

    BatchLoaderService(BatchLoaderRepository repository, BatchLoaderModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    public ResponseEntity<?> findById(int id) {
        try {
            BatchLoader batchLoader = repository.findById(id).orElseThrow(() -> new RuntimeException());
            EntityModel<BatchLoader> entityModel = assembler.toModel(batchLoader);
            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(entityModel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"state\": false}");
        }
    }

    public ResponseEntity<?> findBetweenDates(Date start_date, Date end_date) {
        try {
            List<EntityModel<BatchLoader>> personResolutions = repository.findByCreatedAtBetween(start_date, end_date)
                    .stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());
            CollectionModel<EntityModel<BatchLoader>> collectionModel = CollectionModel.of(personResolutions,
                    linkTo(methodOn(BatchLoaderController.class).all(start_date,
                            end_date)).withSelfRel());
            return ResponseEntity.created(collectionModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(collectionModel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"state\": false}");
        }

    }

    public ResponseEntity<?> newBatchLoaderMetric(BatchLoader newBatchLoader) {
        try {
            EntityModel<BatchLoader> entityModel = assembler.toModel(repository.save(newBatchLoader));
            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body("{\"state\": true}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"state\": false}");
        }
    }

}
