package com.tabjy.cmpt383.project.services;

import com.tabjy.cmpt383.project.models.Fruit;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import org.bson.types.ObjectId;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FruitService implements PanacheMongoRepository<Fruit> {

    public Fruit add(Fruit fruit) {
        persist(fruit);
        return fruit;
    }

    public Fruit lookup(String id) {
        return findById(new ObjectId(id));
    }
}