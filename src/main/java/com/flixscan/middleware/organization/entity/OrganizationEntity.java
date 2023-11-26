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

import java.time.LocalDateTime;

public class OrganizationEntity {

    public Long organization_id;
    public String organization_name;
    public String organization_address;
    public String organization_phone;
    public String organization_email;
    public LocalDateTime created_at;
    public LocalDateTime updated_at;


    public OrganizationEntity() {
        // default constructor.
    }

    public OrganizationEntity(
            Long organization_id, String organization_name,
            String organization_address, String organization_phone,
            String organization_email, LocalDateTime created_at,
            LocalDateTime updated_at) {
        this.organization_id = organization_id;
        this.organization_name = organization_name;
        this.organization_address = organization_address;
        this.organization_phone = organization_phone;
        this.organization_email = organization_email;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public static Multi<OrganizationEntity> findAll(PgPool client) {
        return client.query("SELECT * FROM organization ORDER BY organization_name ASC").execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(OrganizationEntity::from);
    }

    public static Multi<OrganizationEntity> findAllByPage(PgPool client, int limit, int offset) {
        String sql = "SELECT * FROM organization ORDER BY organization_id DESC LIMIT $1 OFFSET $2";
        return client.preparedQuery(sql).execute(Tuple.of(limit, offset))
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(OrganizationEntity::from);
    }
    /*
    public static Uni<OrganizationEntity> findAllById(PgPool client, Long id, int limit, int offset) {
        String sql = "SELECT organization_id, organization_name, organization_address FROM organization WHERE organization_id = $1 LIMIT $2 OFFSET $3";
        return client.preparedQuery(sql).execute(Tuple.of(id, limit, offset))
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }*/


    public static Uni<OrganizationEntity> findById(PgPool client, Long id) {
        return client.preparedQuery("SELECT * FROM organization WHERE organization_id = $1").execute(Tuple.of(id))
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    public Uni<Long> save(PgPool client) {
        return client.preparedQuery("INSERT INTO organization (organization_name, organization_address, organization_phone, organization_email)" +
                        " VALUES ($1, $2, $3, $4) RETURNING organization_id")
                .execute(Tuple.of(organization_name, organization_address, organization_phone, organization_email))
                .onItem().transform(pgRowSet -> pgRowSet.iterator().next().getLong("organization_id"));
    }

    public Uni<Boolean> update(PgPool client, Long organization_id) {
        return client.preparedQuery("UPDATE organization SET organization_name = $1 WHERE organization_id = $2").
                execute(Tuple.of(organization_name, organization_id))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    public static Uni<Boolean> delete(PgPool client, Long organization_id) {
        return client.preparedQuery("DELETE FROM organization WHERE organization_id = $1").execute(Tuple.of(organization_id))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    private static OrganizationEntity from(Row row) {
        return new OrganizationEntity(
                row.getLong("organization_id"),
                row.getString("organization_name"),
                row.getString("organization_address"),
                row.getString("organization_phone"),
                row.getString("organization_email"),
                row.getLocalDateTime("created_at"),
                row.getLocalDateTime("updated_at")
        );
    }
}
