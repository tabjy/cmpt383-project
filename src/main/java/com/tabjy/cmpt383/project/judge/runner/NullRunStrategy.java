package com.tabjy.cmpt383.project.judge.runner;

import com.tabjy.cmpt383.project.judge.ExecResult;

import java.util.Map;

public class NullRunStrategy implements IRunStrategy {
    @Override
    public void setTimeout(long ms) {
        // noop
    }

    @Override
    public void setMemoryLimit(long bytes) {
        // noop
    }

    @Override
    public ExecResult run(Map<String, byte[]> outputFiles, String entryPoint, String[] args) {
        throw new RuntimeException("no build strategy selected");
    }
}
