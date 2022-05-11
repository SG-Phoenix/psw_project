package com.example.fakeestore.entity;

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
public class User {

    @Id
    @SequenceGenerator(name = "user_sequence", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "user_sequence", strategy = GenerationType.SEQUENCE)
    @Column(updatable = false)
    private Long id;

    @Basic
    @Column(name = "e_mail", unique = true, nullable = false)
    @ToString.Exclude
    private String EMail;

    @Basic
    @Column(name = "first_name")
    @ToString.Exclude
    private String firstName;

    @Basic
    @Column(name = "last_name")
    @ToString.Exclude
    private String lastName;

    @Basic
    @Column(name = "username", unique = true, nullable = false, updatable = false)
    private String username;


    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @JsonIgnore
    private List<Order> orders;

    @Basic
    @Version
    @Column(name = "version", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Long version;

}
