package com.example.fakeebay.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Product {

    @Id
    @SequenceGenerator(name = "product_sequence", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequence")
    private Long id;

    @Basic
    @Column(nullable = false)
    private String name;

    @Basic
    @Column(nullable = false)
    private String barcode;

    @Basic
    @Column(nullable = true, name = "description")
    @ToString.Exclude
    private String description;

    @Basic
    @Column(name = "price", nullable = false, precision = 2)
    @ToString.Exclude
    private Float price;

    @Basic
    @Column(name = "qty", nullable = false)
    @ToString.Exclude
    private Integer quantity;

    @Basic
    @Version
    @Column(name = "version", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Long version;

    @ManyToOne()
    @JoinColumn(name = "user",nullable = false, updatable = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "product", cascade = CascadeType.MERGE)
    @JsonIgnore
    @ToString.Exclude
    private List<OrderLine> orderLines;

}
