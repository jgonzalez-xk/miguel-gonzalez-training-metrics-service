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
public class BatchLoader {
    private @Id int id;
    private String file_name;
    private Timestamp created_at;
    private Timestamp file_created_at;
    private Timestamp file_pickedup_at;
    private Timestamp file_processed_at;
    private int num_records;
}
