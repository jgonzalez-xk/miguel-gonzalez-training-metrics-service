package com.xumak.training.metrics.models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
public class PersonResolution {

    private @Id int id;
    private Timestamp created_at;
    private int individual_matches;
    private int household_matches;
    private int nomatches;
    private int errors;

}
