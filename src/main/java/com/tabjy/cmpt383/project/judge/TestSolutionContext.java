package com.tabjy.cmpt383.project.judge;

import com.tabjy.cmpt383.project.judge.builder.BuildStrategies;
import com.tabjy.cmpt383.project.judge.runner.RunStrategies;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TestSolutionContext {
    public static void main(String[] args) throws IOException, LanguageNotSupportedException {
        SolutionContext ctx = new SolutionContext();
        ctx.setBuildStrategy(BuildStrategies.forLanguage("javascript"));
        ctx.setRunStrategy(RunStrategies.forLanguage("javascript"));

        ctx.addSourceFile("main.js", "console.log('hello');console.error('world');process.exit(2);".getBytes(StandardCharsets.UTF_8));
        ExecResult result = ctx.build();
        if (result.exitCode != 0) {
            System.err.println(result);
        }
        result = ctx.run("main.js", new String[0]);
        System.out.println(result);
    }
}
