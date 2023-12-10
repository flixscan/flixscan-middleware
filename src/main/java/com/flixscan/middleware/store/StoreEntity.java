package com.flixscan.middleware.store;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@Entity
@Table(name = "stores")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class StoreEntity extends PanacheEntityBase {
    @Id
    @SequenceGenerator(name = "StoreSeq", sequenceName = "stores_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "StoreSeq")
    private Long id;

    @Column(name = "organization_id")
    private String organizationId;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "store_code")
    private String storeCode;

    @Column(name = "store_country")
    private String storeCountry;

    @Column(name = "store_region")
    private String storeRegion;

    @Column(name = "store_city")
    private String storeCity;

    @Column(name = "store_phone")
    private String storePhone;

    @Column(name = "store_email")
    private String storeEmail;

    @Column(name = "epaper_count")
    private String epaperCount;

    @Column(name = "gateway_count")
    private String gatewayCount;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
