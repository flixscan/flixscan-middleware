/*
 * Copyright (c) 2024 flixscan. All rights reserved.
 */
package com.flixscan.middleware.product;

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

@Path("products")
@Produces("application/json")
@Consumes("application/json")
public class ProductResource {
    private static final Logger LOGGER = Logger.getLogger(ProductResource.class.getName());
    @Inject
    ProductService service;


    @GET
    public Uni<List<ProductEntity>> getAllEntry() {
        return service.getAllProduct();
    }

    public Uni<ProductEntity> getSingleEntry(@PathParam("id") Long id) {
        return service.findItemById(id);
    }

    @GET
    @Path("/{limit}/{offset}")
    // limit= item per page, offset = page number. if 6 element and per page 3 than total 2 page. so limit = 3, page =1 but url param=0 and page 2 but url param= 3
    public Uni<List<ProductEntity>> getEntryByPage(int limit, int offset) {
        return service.findAllByPage(limit, offset);
    }

    @POST
    public Uni<Response> createEntry(ProductEntity product) {
        if (product == null || product.getId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }
        return service.createProduct(product);
    }

    @PUT
    @Path("{key}")
    public Uni<Response> updateEntry(@PathParam("key") Long key, ProductEntity product) {
        if (product == null || product.getProductAttribute() == null) {
            throw new WebApplicationException("Product name was not set on request.", 422);
        }
        return service.updateProduct(key, product);
    }

    @DELETE
    @Path("{key}")
    public Uni<Response> deleteEntry(@PathParam("key") Long key) {
        return service.deleteProduct(key);
    }
}
