package com.flixscan.middleware.rack;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import io.smallrye.mutiny.Uni;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;

import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import org.jboss.logging.Logger;

@Path("racks")
@Produces("application/json")
@Consumes("application/json")
public class RackResource {
    private static final Logger LOGGER = Logger.getLogger(RackResource.class.getName());
    @Inject
    RackService service;


    @GET
    public Uni<List<RackEntity>> getAllEntry() {
        return service.getAllRack();
    }

    public Uni<RackEntity> getSingleEntry(@PathParam("id") Long id) {
        return service.findItemById(id);
    }

    @GET
    @Path("/{limit}/{offset}")
    // limit= item per page, offset = page number. if 6 element and per page 3 than total 2 page. so limit = 3, page =1 but url param=0 and page 2 but url param= 3
    public Uni<List<RackEntity>> getEntryByPage(int limit, int offset) {
        return service.findAllByPage(limit, offset);
    }

    @POST
    public Uni<Response> createEntry(RackEntity rack) {
        if (rack == null || rack.getId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }
        LOGGER.info("New rack is created");
        return service.createRack(rack);
    }

    @PUT
    @Path("{key}")
    public Uni<Response> updateEntry(@PathParam("key") Long key, RackEntity rack) {
        if (rack == null || rack.getRackName() == null) {
            throw new WebApplicationException("Rack name was not set on request.", 422);
        }
        LOGGER.info("A rack is updated with id: " + key);
        return service.updateRack(key, rack);
    }

    @DELETE
    @Path("{key}")
    public Uni<Response> deleteEntry(@PathParam("key") Long key) {
        LOGGER.info("A rack is deleted with id: " + key);
        return service.deleteRack(key);
    }
}
