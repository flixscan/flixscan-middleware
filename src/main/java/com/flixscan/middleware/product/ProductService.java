/*
 * Copyright (c) 2024 flixscan. All rights reserved.
 */
package com.flixscan.middleware.product;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.util.List;

import static jakarta.ws.rs.core.Response.Status.CREATED;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static jakarta.ws.rs.core.Response.Status.NO_CONTENT;

@ApplicationScoped
public class ProductService {

    public Uni<List<ProductEntity>> getAllProduct() {
        return ProductEntity.listAll();
    }

    public Uni<ProductEntity> findItemById(Long id) {
        return ProductEntity.findById(id);
    }

    public Uni<List<ProductEntity>> findAllByPage(int limit, int offset) {
        return ProductEntity.findAll().page(limit, offset).list();
    }

    public Uni<Response> createProduct(ProductEntity product) {
        return Panache.withTransaction(() ->
                product.persist().replaceWith(() -> Response.ok(product).status(CREATED).build()));
    }

    public Uni<Response> updateProduct(Long id, ProductEntity updatedProduct) {
        return Panache.withTransaction(() ->
                updatedProduct.<ProductEntity>findById(id)
                        .onItem().ifNotNull().transform(entity -> {
                            entity.setProductAttribute(updatedProduct.getProductAttribute());
                            entity.setLinkedEpaper(updatedProduct.getLinkedEpaper());
                            entity.setLinkedRack(updatedProduct.getLinkedRack());
                            entity.setStoreId(updatedProduct.getStoreId());
                            entity.setUpdatedAt(Instant.now());
                            entity.persist();
                            return Response.ok(entity).build();
                        }).onItem().ifNull().continueWith(Response.ok().status(NOT_FOUND)::build)
        );
    }

    public Uni<Response> deleteProduct(Long id) {
        return Panache.withTransaction(() -> ProductEntity.deleteById(id))
                .map(deleted -> deleted ? Response.ok().status(NO_CONTENT).build() : Response.ok().status(NOT_FOUND).build());
    }
}

