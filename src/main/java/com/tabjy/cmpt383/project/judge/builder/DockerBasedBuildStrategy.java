package com.tabjy.cmpt383.project.judge.builder;

public abstract class DockerBasedBuildStrategy implements IBuildStrategy {
    abstract String getDockerImageTag();
}
