package com.flixscan.middleware.area;

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
public class AreaService {

    public Uni<List<AreaEntity>> getAllArea() {
        return AreaEntity.listAll();
    }

    public Uni<AreaEntity> findItemById(Long id) {
        return AreaEntity.findById(id);
    }

    public Uni<List<AreaEntity>> findAllByPage(int limit, int offset) {
        return AreaEntity.findAll().page(limit, offset).list();
    }

    public Uni<Response> createArea(AreaEntity area) {
        return Panache.withTransaction(() ->
                area.persist().replaceWith(() -> Response.ok(area).status(CREATED).build()));
    }

    public Uni<Response> updateArea(Long id, AreaEntity updatedArea) {
        return Panache.withTransaction(() ->
                updatedArea.<AreaEntity>findById(id)
                        .onItem().ifNotNull().transform(entity -> {
                            entity.setAreaName(updatedArea.getAreaName());
                            entity.setAreaDescription(updatedArea.getAreaDescription());
                            entity.setAreaCode(updatedArea.getAreaCode());
                            entity.setLinkedRack(updatedArea.getLinkedRack());
                            entity.setEpaperCount(updatedArea.getEpaperCount());
                            entity.setGetwayCount(updatedArea.getGetwayCount());
                            entity.setStoreId(updatedArea.getStoreId());
                            entity.setUpdatedAt(Instant.now());
                            entity.persist();
                            return Response.ok(entity).build();
                        }).onItem().ifNull().continueWith(Response.ok().status(NOT_FOUND)::build)
        );
    }


    public Uni<Response> deleteArea(Long id) {
        return Panache.withTransaction(() -> AreaEntity.deleteById(id))
                .map(deleted -> deleted ? Response.ok().status(NO_CONTENT).build() : Response.ok().status(NOT_FOUND).build());
    }
}
