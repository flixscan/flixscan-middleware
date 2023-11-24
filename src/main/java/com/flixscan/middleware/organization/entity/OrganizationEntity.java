/*
 * Copyright (c) 2024 flixscan. All rights reserved.
 */
package com.flixscan.middleware.organization.entity;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

public class OrganizationEntity {

    public Long organization_id;

    public String organization_name;

    public OrganizationEntity() {
        // default constructor.
    }

    public OrganizationEntity(String organization_name) {
        this.organization_name = organization_name;
    }

    public OrganizationEntity(Long organization_id, String organization_name) {
        this.organization_id = organization_id;
        this.organization_name = organization_name;
    }

    public static Multi<OrganizationEntity> findAll(PgPool client) {
        return client.query("SELECT organization_id, organization_name, organization_address FROM organization ORDER BY organization_name ASC").execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(OrganizationEntity::from);
    }

    public static Uni<OrganizationEntity> findById(PgPool client, Long id) {
        return client.preparedQuery("SELECT organization_id, organization_name, organization_address FROM organization WHERE organization_id = $1").execute(Tuple.of(id))
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    public Uni<Long> save(PgPool client) {
        return client.preparedQuery("INSERT INTO organization (organization_name) VALUES ($1) RETURNING id").execute(Tuple.of(organization_name))
                .onItem().transform(pgRowSet -> pgRowSet.iterator().next().getLong("id"));
    }

    public Uni<Boolean> update(PgPool client) {
        return client.preparedQuery("UPDATE organization SET name = $1 WHERE id = $2").execute(Tuple.of(organization_name, organization_id))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    public static Uni<Boolean> delete(PgPool client, Long organization_id) {
        return client.preparedQuery("DELETE FROM orginization WHERE organization_id = $1").execute(Tuple.of(organization_id))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    private static OrganizationEntity from(Row row) {
        return new OrganizationEntity(row.getLong("organization_id"), row.getString("organization_name"));
    }
}
