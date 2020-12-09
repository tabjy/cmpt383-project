package com.tabjy.cmpt383.project.services;

import com.tabjy.cmpt383.project.models.Problem;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import org.bson.types.ObjectId;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProblemService implements PanacheMongoRepository<Problem> {
}
