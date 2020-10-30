package com.tabjy.cmpt383.project.judge.runner;

public class NativeRunStrategy extends DockerBasedRunStrategy {
    @Override
    String getDockerImageTag() {
        return null;
    }
}
