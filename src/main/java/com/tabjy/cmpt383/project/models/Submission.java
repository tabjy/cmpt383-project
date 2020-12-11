package com.tabjy.cmpt383.project.models;

import io.quarkus.mongodb.panache.MongoEntity;
import org.bson.types.ObjectId;

import java.util.List;

@MongoEntity(collection = "Submissions", database = "cmpt383")
public class Submission {

    public ObjectId id;
    public ObjectId problemId;
    public List<File> files;
    public Language language;
}
