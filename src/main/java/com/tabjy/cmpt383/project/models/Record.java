package com.tabjy.cmpt383.project.models;

import io.quarkus.mongodb.panache.MongoEntity;
import org.bson.types.ObjectId;

@MongoEntity(collection = "Records", database = "cmpt383")
public class Record {

    public ObjectId id;
    public ObjectId problemId;
    public ObjectId solutionId;
    public Acceptance acceptance;
    public String username;
    public String datetime;
    public long runtime;
}
