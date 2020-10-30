package com.tabjy.cmpt383.project.judge;

import com.tabjy.cmpt383.project.judge.builder.BuildStrategies;
import com.tabjy.cmpt383.project.judge.runner.RunStrategies;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TestSolutionContext {
    public static void main(String[] args) throws IOException, LanguageNotSupportedException {
        String source = "#include <iostream>\n" +
                "\n" +
                "int main() {\n" +
                "    std::cout << \"Hello World!\";\n" +
                "    return 0;\n" +
                "}";

        SolutionContext ctx = new SolutionContext();
        ctx.setBuildStrategy(BuildStrategies.forLanguage("c++"));
        ctx.setRunStrategy(RunStrategies.forLanguage("c++"));

        ctx.addSourceFile("main.cpp", source.getBytes(StandardCharsets.UTF_8));
        ExecResult result = ctx.build();
        if (result.exitCode != 0) {
            System.err.println(result);
            return;
        }
        result = ctx.run("main");
        System.out.println(result);
    }
}
