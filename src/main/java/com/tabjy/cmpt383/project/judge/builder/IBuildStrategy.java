package com.tabjy.cmpt383.project.judge.builder;

import com.tabjy.cmpt383.project.judge.ExecResult;

import java.io.IOException;
import java.util.Map;

public interface IBuildStrategy {
    void appendAdditionCompilerFlags(String[] flags);

    ExecResult build(Map<String, byte[]> sourceFiles, Map<String, byte[]> outputFiles) throws IOException;
}
