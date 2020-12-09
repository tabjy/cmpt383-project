package com.tabjy.cmpt383.project.models;

import io.quarkus.mongodb.panache.MongoEntity;
import org.bson.types.ObjectId;

import java.util.*;

@MongoEntity(collection = "Fruits", database = "cmpt383")
public class Fruit {

    public ObjectId id;
    public String name;
    public String description;
    public Point point;
    public List<Point> points = new ArrayList<>();
    public Map<String, Boolean> templates = new HashMap<>();
    public Language language;

    public Fruit() {
    }

    public Fruit(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public ObjectId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Fruit)) {
            return false;
        }

        Fruit other = (Fruit) obj;

        return Objects.equals(other.name, this.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    public static class Point {
        public int x;
        public int y;
    }
}
