package com.tabjy.cmpt383.project.judge.builder;

import com.tabjy.cmpt383.project.judge.ExecResult;

import java.util.Arrays;
import java.util.Map;

public class InterpretedLanguageBuildStrategy implements IBuildStrategy {

    @Override
    public void appendAdditionCompilerFlags(String[] flags) {
        // noop
    }

    @Override
    public ExecResult build(Map<String, byte[]> sourceFiles, Map<String, byte[]> outputFiles) {
        for (Map.Entry<String, byte[]> entry : sourceFiles.entrySet()) {
            outputFiles.put(entry.getKey(), Arrays.copyOf(entry.getValue(), entry.getValue().length));
        }

        return ExecResult.emptySuccess();
    }
}
