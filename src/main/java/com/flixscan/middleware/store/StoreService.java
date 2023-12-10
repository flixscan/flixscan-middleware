package com.flixscan.middleware.store;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.util.List;

import static jakarta.ws.rs.core.Response.Status.*;


@ApplicationScoped
public class StoreService {

    public Uni<List<StoreEntity>> getAllStores() {
        return StoreEntity.listAll();
    }

    public Uni<StoreEntity> findItemById(Long id) {
        return StoreEntity.findById(id);
    }

    public Uni<List<StoreEntity>>findAllByPage(int limit, int offset) {
        return StoreEntity.findAll().page(limit,offset).list();
    }

    public Uni<Response> createStore(StoreEntity store) {
        return Panache.withTransaction(() ->
                store.persist().replaceWith(() -> Response.ok(store).status(CREATED).build()));
    }

    public Uni<Response> updateStore(Long id, StoreEntity store) {
        return Panache.withTransaction(() ->
                store.<StoreEntity>findById(id)
                        .onItem().ifNotNull().transform(entity -> {
                            entity.setOrganizationId(store.getOrganizationId());
                            entity.setStoreName(store.getStoreName());
                            entity.setStoreCode(store.getStoreCode());
                            entity.setStoreCountry(store.getStoreCountry());
                            entity.setStoreRegion(store.getStoreRegion());
                            entity.setStoreCity(store.getStoreCity());
                            entity.setStorePhone(store.getStorePhone());
                            entity.setStoreEmail(store.getStoreEmail());
                            entity.setEpaperCount(store.getEpaperCount());
                            entity.setGatewayCount(store.getGatewayCount());
                            entity.setUpdatedAt(Instant.now());
                            entity.persist();
                            return Response.ok(entity).build();
                        })
                        .onItem().ifNull().continueWith(Response.ok().status(NOT_FOUND)::build)
        );
    }


    public Uni<Response> deleteStore(Long id) {
        return Panache.withTransaction(() -> StoreEntity.deleteById(id))
                .map(deleted -> deleted ? Response.ok().status(NO_CONTENT).build() : Response.ok().status(NOT_FOUND).build());
    }
}