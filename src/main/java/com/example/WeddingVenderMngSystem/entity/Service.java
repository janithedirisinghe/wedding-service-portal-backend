package com.example.WeddingVenderMngSystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    private String name;
    private String description;
    private Double pricing;

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

}
