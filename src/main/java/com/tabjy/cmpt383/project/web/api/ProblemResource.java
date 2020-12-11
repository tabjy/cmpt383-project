package com.tabjy.cmpt383.project.web.api;

import com.tabjy.cmpt383.project.models.Problem;
import com.tabjy.cmpt383.project.models.Record;
import com.tabjy.cmpt383.project.services.ProblemService;
import com.tabjy.cmpt383.project.services.RecordService;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/problems")
public class ProblemResource {

    @Inject
    ProblemService problemService;
    @Inject
    RecordService recordService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Problem> list() {
        return problemService.list();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Problem show(@PathParam String id) {
        return problemService.show(id);
    }

    @GET
    @Path("/{id}/leaderboard")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Record> leaderboard(@PathParam String id) {
        return recordService.listByProblemId(id);
    }
}
