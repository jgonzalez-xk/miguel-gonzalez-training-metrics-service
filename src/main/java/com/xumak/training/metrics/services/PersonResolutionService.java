package com.xumak.training.metrics.services;

import java.util.Date;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import com.xumak.training.metrics.models.PersonResolution;

public interface PersonResolutionService {
    public EntityModel<PersonResolution> findById(int id);

    public CollectionModel<EntityModel<PersonResolution>> findBetweenDates(Date start_date, Date end_date);

    public EntityModel<PersonResolution> newPersonResolution(PersonResolution newPersonResolution);
}
