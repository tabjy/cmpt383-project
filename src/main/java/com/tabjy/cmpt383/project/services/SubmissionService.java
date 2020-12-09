package com.tabjy.cmpt383.project.services;

import com.tabjy.cmpt383.project.judge.ExecResult;
import com.tabjy.cmpt383.project.judge.LanguageNotSupportedException;
import com.tabjy.cmpt383.project.judge.SolutionContext;
import com.tabjy.cmpt383.project.judge.builder.BuildStrategies;
import com.tabjy.cmpt383.project.judge.runner.RunStrategies;
import com.tabjy.cmpt383.project.models.*;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import org.apache.commons.codec.language.bm.Lang;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class SubmissionService implements PanacheMongoRepository<Submission> {
    private static final Map<Language, String> LANGUAGE_TO_OUTPUT_FILENAMES;

    static {
        LANGUAGE_TO_OUTPUT_FILENAMES = Map.of(
                Language.c, "main", //
                Language.cpp, "main", //
                Language.javascript, "main.js", //
                Language.python, "main.py" //
        );
    }

    @Inject
    ProblemService problemService;

    private Result compile(Submission submission, Problem problem, SolutionContext ctx) throws LanguageNotSupportedException, IOException {
        if (!problem.templates.containsKey(submission.language.name())) {
            throw new LanguageNotSupportedException(submission.language.name() + " is not supported for this problem");
        }

        ctx.setBuildStrategy(BuildStrategies.forLanguage(submission.language));
        ctx.setRunStrategy(RunStrategies.forLanguage(submission.language));

        for (File f : problem.templates.get(submission.language.name()).files) {
            ctx.addSourceFile(f.path, f.content.getBytes(StandardCharsets.UTF_8));
        }

        for (File f : submission.files) {
            ctx.addSourceFile(f.path, f.content.getBytes(StandardCharsets.UTF_8));
        }

        Instant then = Instant.now();
        ExecResult result = ctx.build();
        Instant now = Instant.now();

        Result r = new Result();
        r.acceptance = result.exitCode == 0 ? Acceptance.ac : Acceptance.ce;
        r.actual = result.toString();
        r.runtime = Duration.between(then, now).get(ChronoUnit.MILLIS);
        return r;
    }

    public Result testOneCase(Submission submission, SolutionContext ctx, TestCase testCase) throws IOException {
        Instant then = Instant.now();
        ExecResult er = ctx.run(LANGUAGE_TO_OUTPUT_FILENAMES.get(submission.language), null, testCase.in.getBytes(StandardCharsets.UTF_8));
        Instant now = Instant.now();

        Result r = new Result();
        r.input = testCase.in;
        r.expected = testCase.out;
        r.actual = er.getStdout();
        r.runtime = Duration.between(then, now).get(ChronoUnit.MILLIS);
        r.acceptance = Acceptance.ac;
        if (er.exitCode != 0) {
            r.acceptance = Acceptance.re;
        } else if (!r.expected.equals(r.actual)) {
            r.acceptance = Acceptance.wa;
        }

        return r;
    }

    public List<Result> testAllCases(Submission submission) throws IOException, LanguageNotSupportedException {
        Problem problem = problemService.findById(submission.problemId);
        SolutionContext ctx = new SolutionContext();
        Result result = compile(submission, problem, ctx);
        if (result.acceptance != Acceptance.ac) {
            return List.of(result);
        }

        List<Result> results = new ArrayList<>();
        for (TestCase tc : problem.testCases) {
            results.add(testOneCase(submission, ctx, tc));
        }

        return results;
    }
}
