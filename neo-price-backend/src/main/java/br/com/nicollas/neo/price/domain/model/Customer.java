package br.com.nicollas.neo.price.domain.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(nullable = false)
    private String name;

    private String companyName;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
