package com.xumak.training.metrics.services;

import java.util.Date;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import com.xumak.training.metrics.models.BatchLoader;

public interface BatchLoaderService {

    public EntityModel<BatchLoader> findById(int id);

    public CollectionModel<EntityModel<BatchLoader>> findBetweenDates(Date start_date, Date end_date);

    public EntityModel<BatchLoader> newBatchLoader(BatchLoader newBatchLoader);

}
