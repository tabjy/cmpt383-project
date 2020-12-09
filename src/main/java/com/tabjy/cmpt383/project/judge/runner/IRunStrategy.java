package com.tabjy.cmpt383.project.judge.runner;

import com.tabjy.cmpt383.project.judge.ExecResult;

import java.io.IOException;
import java.util.Map;

public interface IRunStrategy {
    void setTimeout(long ms);

    void setMemoryLimit(long bytes);

    ExecResult run(Map<String, byte[]> outputFiles, String entryPoint, String[] args, byte[] stdin) throws IOException;
}
