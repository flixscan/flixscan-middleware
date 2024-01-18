
package com.flixscan.middleware.epaper;
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

@Path("epapers")
@Produces("application/json")
@Consumes("application/json")
public class EpaperResource {
    private static final Logger LOGGER = Logger.getLogger(EpaperResource.class.getName());
    @Inject
    EpaperService service;


    @GET
    public Uni<List<EpaperEntity>> getAllEntry() {
        return service.getAllEpaper();
    }

    public Uni<EpaperEntity> getSingleEntry(@PathParam("id") Long id) {
        return service.findItemById(id);
    }

    @GET
    @Path("/{limit}/{offset}")
    // limit= item per page, offset = page number. if 6 element and per page 3 than total 2 page. so limit = 3, page =1 but url param=0 and page 2 but url param= 3
    public Uni<List<EpaperEntity>> getEntryByPage(int limit, int offset) {
        return service.findAllByPage(limit, offset);
    }

    @POST
    public Uni<Response> createEntry(EpaperEntity epaper) {
        if (epaper == null || epaper.getId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }
        LOGGER.info("New epaper is created");
        return service.createEpaper(epaper);
    }

    @PUT
    @Path("{key}")
    public Uni<Response> updateEntry(@PathParam("key") Long key, EpaperEntity epaper) {
        if (epaper == null || epaper.getProductId() == null) {
            throw new WebApplicationException("Epaper name was not set on request.", 422);
        }
        LOGGER.info("A epaper is updated with id: " + key);
        return service.updateEpaper(key, epaper);
    }

    @DELETE
    @Path("{key}")
    public Uni<Response> deleteEntry(@PathParam("key") Long key) {
        LOGGER.info("A rack is deleted with id: " + key);
        return service.deleteEpaper(key);
    }
}