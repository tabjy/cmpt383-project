package com.tabjy.cmpt383.project.services;

import com.tabjy.cmpt383.project.models.Record;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class RecordService implements PanacheMongoRepository<Record> {

    public Record create(Record record) {
        persist(record);
        return record;
    }

    public List<Record> listByProblemId(String id) {
        return list(new Document().append("problemId", new ObjectId(id)));
    }
}
