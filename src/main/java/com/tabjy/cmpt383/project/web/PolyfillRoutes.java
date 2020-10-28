package com.tabjy.cmpt383.project.web;

import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RoutingExchange;
import org.jboss.logging.Logger;

import java.io.IOException;

import static io.vertx.core.http.HttpMethod.GET;

// FIXME: rewrite to index.html in sub-directories is not happening for some reason.
public class PolyfillRoutes {

    private static final Logger LOG = Logger.getLogger(PolyfillRoutes.class);

    @Route(methods = GET, regex = ".*", order = Integer.MAX_VALUE)
    void index(RoutingExchange re) throws IOException {
        String path = re.context().normalisedPath();
        if (path.charAt(path.length() - 1) == '/') {
            path = path.substring(0, path.length() - 1);
        }
        path += "index.html";

        if (PolyfillRoutes.class.getClassLoader().getResource("META-INF/resources" + path) != null) {
            LOG.debugv("polyfill index.html rewrite for path {0}", re.context().normalisedPath());
            re.context().reroute(path);
            return;
        }

        LOG.debugv("request to {0} resolved a not found", re.context().normalisedPath());
        re.notFound();
        re.context().reroute("/200.html");
    }
}
