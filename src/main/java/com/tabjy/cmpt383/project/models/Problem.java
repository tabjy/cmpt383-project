package com.tabjy.cmpt383.project.models;

import io.quarkus.mongodb.panache.MongoEntity;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MongoEntity(collection = "Problems", database = "cmpt383")
public class Problem {

    public ObjectId id;
    public String title;
    public Difficulty difficulty;
    public String description; // HTML
    public List<TestCase> testCases = new ArrayList<>();
    public Map<String, Template> templates = new HashMap<>();
}
