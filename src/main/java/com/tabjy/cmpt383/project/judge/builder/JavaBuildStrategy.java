package com.tabjy.cmpt383.project.judge.builder;

import java.nio.file.Path;

public class JavaBuildStrategy extends DockerBasedBuildStrategy {

    @Override
    String getContainerImageTag() {
        return "tabjy/cmpt-383-project-builder-openjdk:latest";
    }

    @Override
    Path getContainerWorkDirectory() {
        return Path.of("/work");
    }

    @Override
    String[] getCompilerArgs(Path outputDir, Path sourceDir) {
        return new String[]{
                "javac", "-d", outputDir.toString()
        };
    }
}
