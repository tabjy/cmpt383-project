package com.tabjy.cmpt383.project.judge.builder;

import java.nio.file.Path;

public class CBuildStrategy extends DockerBasedBuildStrategy {

    @Override
    String getContainerImageTag() {
        return "tabjy/cmpt-383-project-builder-gcc:latest";
    }

    @Override
    Path getContainerWorkDirectory() {
        return Path.of("/work");
    }

    @Override
    String[] getCompilerArgs() {
        return new String[]{
                "gcc", "-Wall", "-O2", "-lm", "-pthread", "-fomit-frame-pointer", "-o", "./bin/main"
        };
    }
}