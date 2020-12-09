package com.tabjy.cmpt383.project.web.api;

import com.tabjy.cmpt383.project.models.Fruit;
import com.tabjy.cmpt383.project.services.FruitService;
import com.tabjy.cmpt383.project.web.PolyfillRoutes;
import org.jboss.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/fruits")
public class FruitRoutes {
    private static final Logger LOG = Logger.getLogger(FruitRoutes.class);

    @Inject
    FruitService fruitService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Fruit> list() {
        return fruitService.listAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Fruit add(Fruit fruit) {
        fruitService.add(fruit);
        LOG.debug(fruit.getId());
        return fruit;
    }
}
