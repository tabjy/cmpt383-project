package com.tabjy.cmpt383.project.services;

import com.tabjy.cmpt383.project.models.Problem;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import org.bson.types.ObjectId;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProblemService implements PanacheMongoRepository<Problem> {

    static Problem removeHiddenTestCases(Problem p) {
        p.testCases = p.testCases.stream().filter(tc -> !tc.hidden).collect(Collectors.toList());
        return p;
    }

    public List<Problem> list() {
        return listAll().stream().map(ProblemService::removeHiddenTestCases).collect(Collectors.toList());
    }

    public Problem show(String id) {
        return removeHiddenTestCases(findById(new ObjectId(id)));
    }
}
