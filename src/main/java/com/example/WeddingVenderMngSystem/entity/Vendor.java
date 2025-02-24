package com.example.WeddingVenderMngSystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "venders")
@Getter
@Setter
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long venderId;

    private String businessName;
    private String availability;

    private String Location;
    private  String BRN;
    private String Country;
    private String VenType;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
    private List<Service> services;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;
}