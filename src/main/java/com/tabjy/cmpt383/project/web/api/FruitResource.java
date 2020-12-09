package com.tabjy.cmpt383.project.web.api;

import com.tabjy.cmpt383.project.models.Fruit;
import com.tabjy.cmpt383.project.services.FruitService;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.*;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/fruits")
public class FruitResource {

    @Inject
    FruitService fruitService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Fruit> list() throws InterruptedException {
        return fruitService.listAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Fruit create(Fruit fruit) {
        return fruitService.add(fruit);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Fruit show(@PathParam String id) {
        return fruitService.lookup(id);
    }
}
