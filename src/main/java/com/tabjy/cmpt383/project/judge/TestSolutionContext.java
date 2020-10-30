package com.tabjy.cmpt383.project.judge;

import com.tabjy.cmpt383.project.judge.builder.InterpretedLanguageBuildStrategy;
import com.tabjy.cmpt383.project.judge.runner.NativeOnHostRunStrategy;
import com.tabjy.cmpt383.project.judge.runner.NativeRunStrategy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TestSolutionContext {
    public static void main(String[] args) throws IOException {
        SolutionContext ctx = new SolutionContext();
        ctx.setBuildStrategy(new InterpretedLanguageBuildStrategy());
//        ctx.setRunStrategy(new NativeOnHostRunStrategy());
        ctx.setRunStrategy(new NativeRunStrategy());

        ctx.addSourceFile("main", "#!/usr/bin/bash\necho hello".getBytes(StandardCharsets.UTF_8));
        ctx.build();
        ExecResult result = ctx.run("main", new String[0]);
        System.out.println(result);
    }
}
