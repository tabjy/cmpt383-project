package com.tabjy.cmpt383.project.judge;

import com.tabjy.cmpt383.project.judge.builder.IBuildStrategy;
import com.tabjy.cmpt383.project.judge.runner.IRunStrategy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SolutionContext {
    private final Map<String, byte[]> sourceFiles = new HashMap<>();
    private final Map<String, byte[]> outputFiles = new HashMap<>();

    private IBuildStrategy buildStrategy;
    private IRunStrategy runStrategy;

    public void addSourceFile(String path, byte[] content) {
        sourceFiles.put(path, content);
    }

    public byte[] getSourceFile(String path) {
        return sourceFiles.get(path);
    }

    public void addOutputFile(String path, byte[] content) {
        outputFiles.put(path, content);
    }

    public byte[] getOutputFile(String path) {
        return outputFiles.get(path);
    }

    public void setBuildStrategy(IBuildStrategy buildStrategy) {
        this.buildStrategy = buildStrategy;
    }

    public void setRunStrategy(IRunStrategy runStrategy) {
        this.runStrategy = runStrategy;
    }

    public ExecResult build() throws IOException {
        return build(new String[0]);
    }

    public ExecResult build(String[] additionalCompilerFlags) throws IOException {
        return buildStrategy.build(additionalCompilerFlags, sourceFiles, outputFiles);
    }

    public ExecResult run(String entryPoint) throws IOException {
        return run(entryPoint, new String[0], new byte[0]);
    }

    public ExecResult run(String entryPoint, String[] args, byte[] stdin) throws IOException {
        return runStrategy.run(outputFiles, entryPoint, args, stdin);
    }
}
