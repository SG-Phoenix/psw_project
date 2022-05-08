package com.example.fakeebay.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "order_line",uniqueConstraints = {@UniqueConstraint(name = "unique_per_order", columnNames = {"product_id","order_id"})})
public class OrderLine
{

    @Id
    @SequenceGenerator(name = "order_line_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_line_sequence")
    private Long id;

    @Basic
    private Integer quantity;

    @Column(name = "purchase_price")
    private Float purchasePrice;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id", updatable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id", updatable = false)
    @JsonIgnore
    private Order order;

}
