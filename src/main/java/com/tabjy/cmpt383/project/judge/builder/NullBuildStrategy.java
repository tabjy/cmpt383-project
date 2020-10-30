package com.tabjy.cmpt383.project.judge.builder;

import com.tabjy.cmpt383.project.judge.ExecResult;

import java.util.Map;

public class NullBuildStrategy implements IBuildStrategy {

    @Override
    public ExecResult build(String[] additionalCompilerFlags, Map<String, byte[]> sourceFiles, Map<String, byte[]> outputFiles) {
        throw new RuntimeException("no build strategy selected");
    }
}
