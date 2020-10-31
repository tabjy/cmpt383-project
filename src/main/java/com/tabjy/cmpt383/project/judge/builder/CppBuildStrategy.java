package com.tabjy.cmpt383.project.judge.builder;

import java.nio.file.Path;

public class CppBuildStrategy extends DockerBasedBuildStrategy {

    @Override
    String getContainerImageTag() {
        return "tabjy/cmpt-383-project-builder-gcc:latest";
    }

    @Override
    Path getContainerWorkDirectory() {
        return Path.of("/work");
    }

    @Override
    String[] getCompilerArgs(Path outputDir, Path sourceDir) {
        return new String[]{
                "g++", "-Wall", "-O2", "-lm", "-pthread", "-fomit-frame-pointer", "-o", outputDir.resolve("main").toString()
        };
    }
}
