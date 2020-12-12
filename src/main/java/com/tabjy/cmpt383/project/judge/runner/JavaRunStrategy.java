package com.tabjy.cmpt383.project.judge.runner;

import com.tabjy.cmpt383.project.judge.ExecResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;

public class JavaRunStrategy extends DockerBasedRunStrategy {
    @Override
    String getContainerImageTag() {
        return "tabjy/cmpt-383-project-runner-openjdk:latest";
    }

    @Override
    Path getContainerWorkDirectory() {
        return Path.of("/work");
    }

    @Override
    String[] getInterpreterArgs() {
        return new String[0]; // we launch from a .sh wrapper
    }

    @Override
    public ExecResult run(Map<String, byte[]> outputFiles, String entryPoint, String[] args, byte[] stdin) throws IOException {
        outputFiles.put("main.sh", "#!/bin/sh\njava -cp $(dirname $0) Main \"$1\" \"$2\" \"$3\" \"$4\" \"$5\" \"$6\"".getBytes(StandardCharsets.UTF_8));

        return super.run(outputFiles, entryPoint, args, stdin);
    }
}
