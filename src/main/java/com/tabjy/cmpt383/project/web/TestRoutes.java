package com.tabjy.cmpt383.project.web;

import io.quarkus.vertx.web.Route;

import javax.inject.Singleton;

import static io.vertx.core.http.HttpMethod.GET;

@Singleton
public class TestRoutes {
    @Route(methods = GET)
    String test() {
        return "it worked";
    }
}
