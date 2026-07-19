package com.assignment.asm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "Category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    @NotBlank(message = "Tên category không được để trống")
    @Size(max = 100)
    @Column(
            nullable = false,
            unique = true,
            length = 100,
            columnDefinition = "nvarchar(150)"
    )
    private String name;



    @Size(max = 255)
    @Column(length = 255)
    private String description;



    @OneToMany(
            mappedBy = "category",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<Food> foods = new ArrayList<>();

}