package com.tabjy.cmpt383.project.web.api;

import com.tabjy.cmpt383.project.judge.ExecResult;
import com.tabjy.cmpt383.project.judge.LanguageNotSupportedException;
import com.tabjy.cmpt383.project.judge.SolutionContext;
import com.tabjy.cmpt383.project.judge.builder.BuildStrategies;
import com.tabjy.cmpt383.project.judge.runner.RunStrategies;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RoutingExchange;
import io.vertx.core.http.HttpMethod;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class EvaluationRoutes {
    private static final Map<String, String> LANGUAGE_TO_SOURCE_FILENAMES;
    private static final Map<String, String> LANGUAGE_TO_OUTPUT_FILENAMES;

    static {
        LANGUAGE_TO_SOURCE_FILENAMES = Map.of(
                "c", "main.c", //
                "cpp", "main.cpp", //
                "javascript", "main.js", //
                "python", "main.py" //
        );

        LANGUAGE_TO_OUTPUT_FILENAMES = Map.of(
                "c", "main", //
                "cpp", "main", //
                "javascript", "main.js", //
                "python", "main.py" //
        );
    }

    // FIXME: POST is probably better
    @Route(methods = HttpMethod.GET, path = "/api/evaluate", type = Route.HandlerType.BLOCKING)
    public void evaluate(RoutingExchange re) {
        re.response().putHeader("Access-Control-Allow-Origin", "*"); // FIXME: debug only!

        Optional<String> language = re.getParam("language");
        Optional<String> value = re.getParam("value");

        if (language.isEmpty()) {
            re.response().setStatusCode(400).end("language is not specified");
            return;
        }
        if (value.isEmpty()) {
            re.response().setStatusCode(400).end("empty source content");
            return;
        }

        SolutionContext ctx = new SolutionContext();
        try {
            ctx.setBuildStrategy(BuildStrategies.forLanguage(language.get()));
            ctx.setRunStrategy(RunStrategies.forLanguage(language.get()));
        } catch (LanguageNotSupportedException e) {
            re.response().setStatusCode(400).end(e.getMessage());
            return;
        }

        ctx.addSourceFile(
                LANGUAGE_TO_SOURCE_FILENAMES.get(language.get()),
                value.get().getBytes(StandardCharsets.UTF_8)
        );

        ExecResult result;
        try {
            result = ctx.build();
        } catch (IOException e) {
            re.response().setStatusCode(500).end(e.getMessage());
            return;
        }

        if (result.exitCode != 0) {
            re.response().setStatusCode(200).end(result.toString());
            return;
        }

        try {
            result = ctx.run(LANGUAGE_TO_OUTPUT_FILENAMES.get(language.get()));
        } catch (IOException e) {
            re.response().setStatusCode(500).end(e.getMessage());
            return;
        }

        re.response().setStatusCode(200).end(result.toString());
    }
}
