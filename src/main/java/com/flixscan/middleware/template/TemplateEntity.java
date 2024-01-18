/*
 * Copyright (c) 2024 flixscan. All rights reserved.
 */
package com.flixscan.middleware.template;
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
@Table(name = "template")
@EqualsAndHashCode(callSuper = false)
public class TemplateEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "template_name")
    private String templateName;

    @Column(name = "template_details")
    private String templateDetails;

    @Column(name = "template_attribute")
    private String templateAttribute;

    @Column(name = "linked_product")
    private String linkedProduct;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private Instant createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private Instant updatedAt;
}