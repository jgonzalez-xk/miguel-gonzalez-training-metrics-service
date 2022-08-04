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
public class BatchLoader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fileName;
    private Timestamp createdAt = new Timestamp(new Date().getTime());;
    private Timestamp fileCreatedAt;
    private Timestamp filePickedupAt;
    private Timestamp fileProcessedAt;
    private int numRecords;

    // public BatchLoader(String fileName, Timestamp fileCreatedAt, Timestamp
    // filePickedupAt,
    // Timestamp fileProcessedAt, int numRecords) {

    // this.fileName = fileName;
    // this.fileCreatedAt = fileCreatedAt;
    // this.filePickedupAt = filePickedupAt;
    // this.fileProcessedAt = fileProcessedAt;
    // this.numRecords = numRecords;
    // }

    public BatchLoader(String fileName) {
        this.fileName = fileName;
    }

}
