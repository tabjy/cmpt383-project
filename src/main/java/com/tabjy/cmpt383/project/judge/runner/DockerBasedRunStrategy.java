package com.tabjy.cmpt383.project.judge.runner;

import com.tabjy.cmpt383.project.judge.ExecResult;

import java.util.Map;

public abstract class DockerBasedRunStrategy implements IRunStrategy {
    abstract String getDockerImageTag();

    @Override
    public void setTimeout(long ms) {
    }

    @Override
    public void setMemoryLimit(long bytes) {
    }

    @Override
    public ExecResult run(Map<String, byte[]> outputFiles, String entryPoint, String[] args) {
        return null;
    }
}
