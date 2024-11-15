package com.example.btl.btl.models;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "shoe_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoeDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT(11)")
    private int id;
    @Column(name = "color", columnDefinition = "VARCHAR(255)")
    private String color;
    @Column(name = "size", columnDefinition = "INT(3)")
    private int size;
    @Column(name = "shoe_id", columnDefinition = "INT(11)")
    private int shoeId;
}