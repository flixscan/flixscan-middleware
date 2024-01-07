package com.flixscan.middleware.rack;
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
public class RackService {

    public Uni<List<RackEntity>> getAllRack() {
        return RackEntity.listAll();
    }

    public Uni<RackEntity> findItemById(Long id) {
        return RackEntity.findById(id);
    }

    public Uni<List<RackEntity>> findAllByPage(int limit, int offset) {
        return RackEntity.findAll().page(limit, offset).list();
    }

    public Uni<Response> createRack(RackEntity rack) {
        return Panache.withTransaction(() ->
                rack.persist().replaceWith(() -> Response.ok(rack).status(CREATED).build()));
    }

    public Uni<Response> updateRack(Long id, RackEntity updatedRack) {
        return Panache.withTransaction(() ->
                updatedRack.<RackEntity>findById(id)
                        .onItem().ifNotNull().transform(entity -> {
                            entity.setRackName(updatedRack.getRackName());
                            entity.setRackDetails(updatedRack.getRackDetails());
                            entity.setRackArea(updatedRack.getRackArea());
                            entity.setRackImage(updatedRack.getRackImage());
                            entity.setEpaperCount(updatedRack.getEpaperCount());
                            entity.setGetwayCount(updatedRack.getGetwayCount());
                            entity.setStoreId(updatedRack.getStoreId());
                            entity.setUpdatedAt(Instant.now());
                            entity.persist();
                            return Response.ok(entity).build();
                        }).onItem().ifNull().continueWith(Response.ok().status(NOT_FOUND)::build)
        );
    }
    public Uni<Response> deleteRack(Long id) {
        return Panache.withTransaction(() -> RackEntity.deleteById(id))
                .map(deleted -> deleted ? Response.ok().status(NO_CONTENT).build() : Response.ok().status(NOT_FOUND).build());
    }
}
