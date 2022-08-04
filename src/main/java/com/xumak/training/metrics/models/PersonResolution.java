package com.xumak.training.metrics.models;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PersonResolution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Timestamp createdAt = new Timestamp(new Date().getTime());
    private int individualMatches;
    private int householdMatches;
    private int nomatches;
    private int errors;

    public PersonResolution(int individualMatches, int householdMatches, int nomatches, int errors) {
        this.individualMatches = individualMatches;
        this.householdMatches = householdMatches;
        this.nomatches = nomatches;
        this.errors = errors;
    }

}
