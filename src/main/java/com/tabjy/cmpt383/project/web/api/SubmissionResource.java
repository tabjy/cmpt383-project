package com.tabjy.cmpt383.project.web.api;

import com.tabjy.cmpt383.project.judge.LanguageNotSupportedException;
import com.tabjy.cmpt383.project.models.Result;
import com.tabjy.cmpt383.project.models.Submission;
import com.tabjy.cmpt383.project.models.Record;
import com.tabjy.cmpt383.project.services.SubmissionService;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Path("/api/submission")
public class SubmissionResource {

    @Inject
    SubmissionService submissionService;

    @POST
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Result> test(Submission submission) throws IOException, LanguageNotSupportedException {
        return submissionService.testSubmission(submission);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Record create(Submission submission, @QueryParam("username") String username, @QueryParam("email") String email) throws IOException, LanguageNotSupportedException {
        String gravatarId = email;
        if (gravatarId == null || gravatarId.isBlank()) {
            gravatarId = username;
        }

        return submissionService.gradeSubmission(submission, username, gravatarId);
    }
}
