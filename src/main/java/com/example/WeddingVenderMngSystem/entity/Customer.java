package com.example.WeddingVenderMngSystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    private String location;
    private Date weddingDate;
    private Double budget;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}