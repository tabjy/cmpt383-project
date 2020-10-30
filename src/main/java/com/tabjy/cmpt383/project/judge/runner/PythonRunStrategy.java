package com.tabjy.cmpt383.project.judge.runner;

import java.nio.file.Path;

public class PythonRunStrategy extends DockerBasedRunStrategy {
    @Override
    String getContainerImageTag() {
        return "tabjy/cmpt-383-project-runner-python:latest";
    }

    @Override
    Path getContainerWorkDirectory() {
        return Path.of("/work");
    }

    @Override
    String[] getInterpreterArgs() {
        return new String[]{"python3"};
    }
}
