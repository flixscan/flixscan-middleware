/*
 * Copyright (c) 2024 flixscan. All rights reserved.
 */
package com.flixscan.middleware.template;

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
public class TemplateService {

//    private static final Jsonb JSONB = JsonbBuilder.create();

    public Uni<List<TemplateEntity>> getAllTemplate() {
        return TemplateEntity.listAll();
    }

    public Uni<TemplateEntity> findItemById(Long id) {
        return TemplateEntity.findById(id);
    }

    public Uni<List<TemplateEntity>> findAllByPage(int limit, int offset) {
        return TemplateEntity.findAll().page(limit, offset).list();
    }

    public Uni<Response> createTemplate(TemplateEntity template) {
        return Panache.withTransaction(() ->
                template.persist().replaceWith(() -> Response.ok(template).status(CREATED).build()));
    }

    public Uni<Response> updateTemplate(Long id, TemplateEntity updatedTemplate) {
        return Panache.withTransaction(() ->
                updatedTemplate.<TemplateEntity>findById(id)
                        .onItem().ifNotNull().transform(entity -> {
                            entity.setTemplateAttribute(updatedTemplate.getTemplateName());
                            entity.setTemplateDetails(updatedTemplate.getTemplateDetails());
                            entity.setTemplateAttribute(updatedTemplate.getTemplateAttribute());
                            entity.setLinkedProduct(updatedTemplate.getLinkedProduct());
                            entity.setUpdatedAt(Instant.now());
                            entity.persist();
                            return Response.ok(entity).build();
                        }).onItem().ifNull().continueWith(Response.ok().status(NOT_FOUND)::build)
        );
    }

    public Uni<Response> deleteTemplate(Long id) {
        return Panache.withTransaction(() -> TemplateEntity.deleteById(id))
                .map(deleted -> deleted ? Response.ok().status(NO_CONTENT).build() : Response.ok().status(NOT_FOUND).build());
    }

//    public static <T> String serializeToJson(final T bean) {
//        return bean != null ? JSONB.toJson(bean) : null;
//    }
}
