package com.xumak.training.metrics.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import com.xumak.training.metrics.controllers.BatchLoaderController;
import com.xumak.training.metrics.data.BatchLoaderRepository;
import com.xumak.training.metrics.models.BatchLoader;
import com.xumak.training.metrics.services.BatchLoaderService;
import com.xumak.training.metrics.utils.BatchLoaderModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BatchLoaderServiceImpl implements BatchLoaderService {
    private final BatchLoaderRepository repository;
    private final BatchLoaderModelAssembler assembler;

    BatchLoaderServiceImpl(BatchLoaderRepository repository, BatchLoaderModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    public EntityModel<BatchLoader> findById(int id) {
        BatchLoader batchLoader = repository.findById(id).orElseThrow(() -> new RuntimeException());
        return assembler.toModel(batchLoader);
    }

    public CollectionModel<EntityModel<BatchLoader>> findBetweenDates(Date start_date, Date end_date) {
        List<EntityModel<BatchLoader>> personResolutions = repository.findByCreatedAtBetween(start_date, end_date)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(personResolutions,
                linkTo(methodOn(BatchLoaderController.class).all(start_date,
                        end_date)).withSelfRel());
    }

    public EntityModel<BatchLoader> newBatchLoaderMetric(BatchLoader newBatchLoader) {
        return assembler.toModel(repository.save(newBatchLoader));
    }

}
