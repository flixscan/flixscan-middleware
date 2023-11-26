/*
 * Copyright (c) 2024 flixscan. All rights reserved.
 */
package com.flixscan.middleware.organization.boundary;

import java.net.URI;

import com.flixscan.middleware.organization.control.OrganizationService;
import com.flixscan.middleware.organization.entity.OrganizationEntity;
import jakarta.inject.Inject;
import jakarta.json.Json;

import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;


@Path("organization")
public class OrganizationResource {

    private final PgPool client;
    public OrganizationResource(PgPool client) {
        this.client = client;
    }

    @GET
    public Multi<OrganizationEntity> get() {
        return OrganizationEntity.findAll(client);
    }

    @GET
    @Path("/{limit}/{offset}") // limit= item per page, offset = page number. if 6 element and per page 3 than total 2 page. so limit = 3, page =1 but url param=0 and page 2 but url param= 3
    public Multi<OrganizationEntity> get(int limit, int  offset) {
        return OrganizationEntity.findAllByPage(client, limit, offset);
    }

    @GET
    @Path("/{id}")
    public Uni<Response> getSingle(Long id) {
        return OrganizationEntity.findById(client, id)
                .onItem().transform(organization -> {
                    if (organization != null) {
                        return Response.ok(organization);
                    } else {
                        JsonObject errorJson = Json.createObjectBuilder()
                                .build();
                        return Response.status(Status.NOT_FOUND)
                                .entity(errorJson);
                    }
                })
                .onItem().transform(ResponseBuilder::build);
    }

    @POST
    public Uni<Response> create(OrganizationEntity organization) {
        return organization.save(client)
                .onItem().transform(id -> URI.create("/organization/" + id))
                .onItem().transform(uri -> Response.created(uri).build());
    }

    @PUT
    @Path("{id}")
    public Uni<Response> update(Long id, OrganizationEntity organization) {
        return organization.update(client, id)
                .onItem().transform(updated -> updated ? Status.OK : Status.NOT_FOUND)
                .onItem().transform(status -> Response.status(status).build());
    }

    @DELETE
    @Path("{id}")
    public Uni<Response> delete(Long id) {
        return OrganizationEntity.delete(client, id)
                .onItem().transform(deleted -> deleted ? Status.NO_CONTENT : Status.NOT_FOUND)
                .onItem().transform(status -> Response.status(status).build());
    }
}
