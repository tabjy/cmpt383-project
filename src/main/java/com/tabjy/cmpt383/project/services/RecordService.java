package com.tabjy.cmpt383.project.services;

import com.tabjy.cmpt383.project.models.Record;
import io.quarkus.mongodb.panache.PanacheMongoRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RecordService implements PanacheMongoRepository<Record> {

    public Record create(Record record) {
        persist(record);
        return record;
    }
}
