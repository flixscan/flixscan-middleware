package com.flixscan.middleware.area;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@Entity
@Table(name = "area")
@EqualsAndHashCode(callSuper = false)
public class AreaEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "area_name", nullable = false)
    private String areaName;

    @Column(name = "area_description")
    private String areaDescription;

    @Column(name = "area_code")
    private String areaCode;

    @Column(name = "linked_rack")
    private String linkedRack;

    @Column(name = "epaper_count")
    private String epaperCount;

    @Column(name = "getway_count")
    private String getwayCount;

    @Column(name = "store_id", nullable = false)
    private String storeId;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private Instant createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private Instant updatedAt;
}