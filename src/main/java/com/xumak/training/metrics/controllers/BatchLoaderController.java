package com.xumak.training.metrics.controllers;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.xumak.training.metrics.data.BatchLoaderRepository;
import com.xumak.training.metrics.models.BatchLoader;
import com.xumak.training.metrics.utils.BatchLoaderModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class BatchLoaderController {

    private final BatchLoaderRepository repository;
    private final BatchLoaderModelAssembler assembler;

    BatchLoaderController(BatchLoaderRepository repository, BatchLoaderModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/metrics/batch-loader-metric/{id}")
    public EntityModel<BatchLoader> one(@PathVariable int id) {

        BatchLoader batchLoader = repository.findById(id).orElseThrow(() -> new RuntimeException());
        return assembler.toModel(batchLoader);
    }

    @GetMapping("/metrics/batch-loader-metric")
    public CollectionModel<EntityModel<BatchLoader>> all(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start_date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end_date) {

        List<EntityModel<BatchLoader>> personResolutions = repository.findByCreatedAtBetween(start_date, end_date)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(personResolutions,
                linkTo(methodOn(BatchLoaderController.class).all(start_date,
                        end_date)).withSelfRel());
    }

    @PostMapping("/metrics/batch-loader-metric")
    ResponseEntity<?> newBatchLoaderMetric(@RequestBody BatchLoader newBatchLoader) {
        try {
            EntityModel<BatchLoader> entityModel = assembler.toModel(repository.save(newBatchLoader));
            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body("{\"state\": true}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"state\": false}");
        }
    }

}
