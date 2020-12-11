package com.tabjy.cmpt383.project.models;

import io.quarkus.mongodb.panache.MongoEntity;
import org.bson.types.ObjectId;

import java.util.Date;

@MongoEntity(collection = "Records", database = "cmpt383")
public class Record {

    public ObjectId id;
    public ObjectId problemId;
    public ObjectId submissionId;
    public Language language;
    public Acceptance acceptance;
    public String username;
    public String gravatarHash;
    public Date datetime;
    public long runtime;
}
