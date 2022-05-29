package com.example.fakeestore.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "orders")
public class Order
{

    @Id
    @SequenceGenerator(name = "order_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    @JsonProperty(value = "products")
    private List<OrderLine> productsList;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "purchase_time")
    @JsonProperty(value = "purchaseTime")
    private Date creationDate;

    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false, name = "postal_code")
    private String postalCode;
    @Column(nullable = false)
    private String street;

    private Double totalPrice;
};
