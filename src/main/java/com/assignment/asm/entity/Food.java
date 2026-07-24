package com.assignment.asm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "Food")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Food {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotBlank(message = "Tên món không được để trống")
    @Size(max = 150)
    @Column(nullable = false, length = 150, columnDefinition = "nvarchar(150)")
    private String name;



    @Lob
    @Column(columnDefinition = "nvarchar(max)")
    private String description;



    @NotNull(message = "Giá không được để trống")
    @DecimalMin(
            value = "0.0",
            message = "Giá phải lớn hơn hoặc bằng 0"
    )
    private Double price;



    @NotNull(message = "Số lượng không được để trống")
    @Min(
            value = 0,
            message = "Số lượng không được âm"
    )
    private Integer quantity;



    @Column(length = 500)
    private String imageUrl;



    @Builder.Default
    private Boolean status = true;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            nullable = false
    )
    @NotNull(message = "Phải chọn category")
    private Category category;

}
