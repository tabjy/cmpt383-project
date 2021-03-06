package com.tabjy.cmpt383.project.services;

import com.tabjy.cmpt383.project.judge.ExecResult;
import com.tabjy.cmpt383.project.judge.LanguageNotSupportedException;
import com.tabjy.cmpt383.project.judge.SolutionContext;
import com.tabjy.cmpt383.project.judge.builder.BuildStrategies;
import com.tabjy.cmpt383.project.judge.runner.RunStrategies;
import com.tabjy.cmpt383.project.models.Record;
import com.tabjy.cmpt383.project.models.*;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@ApplicationScoped
public class SubmissionService implements PanacheMongoRepository<Submission> {
    private static final Map<Language, String> LANGUAGE_TO_OUTPUT_FILENAMES;

    static {
        LANGUAGE_TO_OUTPUT_FILENAMES = Map.of(
                Language.c, "main", //
                Language.cpp, "main", //
                Language.java, "main.sh", //
                Language.javascript, "main.js", //
                Language.python, "main.py" //
        );
    }

    private static final Logger LOG = Logger.getLogger(SubmissionService.class);

    @Inject
    ProblemService problemService;
    @Inject
    RecordService recordService;

    private Result compile(Submission submission, Problem problem, SolutionContext ctx) throws LanguageNotSupportedException, IOException {
        Optional<Template> template = problem.templates.stream().filter(t -> t.language == submission.language).findFirst();
        if (template.isEmpty()) {
            throw new LanguageNotSupportedException(submission.language.name());
        }

        ctx.setBuildStrategy(BuildStrategies.forLanguage(submission.language));
        ctx.setRunStrategy(RunStrategies.forLanguage(submission.language));

        for (File f : template.get().files) {
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
        r.runtime = Duration.between(then, now).toMillis();
        return r;
    }

    public Result testOneCase(Submission submission, SolutionContext ctx, TestCase testCase) throws IOException {
        Instant then = Instant.now();
        ExecResult er = ctx.run(LANGUAGE_TO_OUTPUT_FILENAMES.get(submission.language), testCase.in.toArray(String[]::new), new byte[0]);
        Instant now = Instant.now();

        System.out.println(er.toString());

        Result r = new Result();
        r.input = testCase.in;
        r.expected = testCase.out;
        r.actual = er.getStdout();
        r.runtime = Duration.between(then, now).toMillis();
        r.acceptance = Acceptance.ac;
        if (er.exitCode != 0) {
            r.acceptance = Acceptance.re;
        } else if (!r.expected.trim().equals(r.actual.trim())) {
            r.acceptance = Acceptance.wa;
        }

        return r;
    }

    private List<Result> testAllTestCases(Submission submission, boolean includeHidden) throws IOException, LanguageNotSupportedException {
        Problem problem = problemService.findById(submission.problemId);
        SolutionContext ctx = new SolutionContext();
        Result result = compile(submission, problem, ctx);
        if (result.acceptance != Acceptance.ac) {
            return List.of(result);
        }

        List<Result> results = new ArrayList<>();
        List<TestCase> testCases = includeHidden ? ProblemService.removeHiddenTestCases(problem).testCases : problem.testCases;
        for (TestCase tc : testCases) {
            results.add(testOneCase(submission, ctx, tc));
        }

        return results;
    }

    public List<Result> testSubmission(Submission submission) throws IOException, LanguageNotSupportedException {
        return testAllTestCases(submission, false);
    }

    public Record gradeSubmission(Submission submission, String username, String gravatarId) throws IOException, LanguageNotSupportedException {
        this.persist(submission);
        Record record = new Record();
        record.problemId = submission.problemId;
        record.submissionId = submission.id;
        record.language = submission.language;
        record.acceptance = Acceptance.ac;
        record.username = username;
        record.gravatarHash = "00000000000000000000000000000000";
        record.datetime = new Date();
        record.runtime = -1;

        List<Result> results = testAllTestCases(submission, true);
        for (Result r : results) {
            if (r.acceptance != Acceptance.ac) {
                record.acceptance = r.acceptance;
            }
        }

        if (record.acceptance == Acceptance.ac) {
            for (Result r : results) {
                if (r.runtime > record.runtime) {
                    record.runtime = r.runtime;
                }
            }
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(gravatarId.trim().toLowerCase().getBytes());
            byte[] digest = md.digest();
            record.gravatarHash = DatatypeConverter.printHexBinary(digest).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            LOG.warn("cannot perform md5", e);
        }

        return recordService.create(record);
    }
}
