package com.flixscan.middleware.epaper;
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
@Table(name = "Epaper")
@EqualsAndHashCode(callSuper = false)
public class EpaperEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "identity_id")
    private String IdentityId;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "linked_gateway")
    private String linkedGateway;

    @Column(name = "battery_status")
    private String batteryStatus;

    @Column(name = "process_status")
    private String processStatus;

    @Column(name = "network_status")
    private String networkStatus;

    @Column(name = "signal_strength")
    private String signalStrength;

    @Column(name = "is_removed")
    private String isRemoved;

    @Column(name = "label_technology")
    private String labelTechnology;

    @Column(name = "removed_at")
    private Instant removedAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private Instant createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private Instant updatedAt;
}