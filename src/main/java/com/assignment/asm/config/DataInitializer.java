package com.assignment.asm.config;

import com.assignment.asm.entity.Category;
import com.assignment.asm.entity.Food;
import com.assignment.asm.service.CategoryService;
import com.assignment.asm.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CategoryService cateSercive;

    @Autowired
    private FoodService foodService;


    @Override
    public void run(String... args) throws Exception {

        if(foodService.getAllFood().size() > 0){
            return;
        }

        Category c1 = Category.builder()
                .name("Thịt")
                .description("Các món thịt nướng BBQ")
                .build();



        Category c2 = Category.builder()
                .name("Hải sản")
                .description("Các món hải sản nướng")
                .build();



        Category c3 = Category.builder()
                .name("Rau củ")
                .description("Các loại rau củ nướng")
                .build();



        cateSercive.createCategory(c1);
        cateSercive.createCategory(c2);
        cateSercive.createCategory(c3);

        Food f1 = Food.builder()
                .name("Ba chỉ bò Mỹ nướng BBQ")
                .description("Thịt bò Mỹ thái lát nướng cùng sốt BBQ")
                .price(150000D)
                .quantity(50)
                .imageUrl("https://res.cloudinary.com/kkskswrh/image/upload/v1784196412/bo_hmpzwj.jpg")
                .status(true)
                .category(c1)
                .build();



        Food f2 = Food.builder()
                .name("Sườn heo nướng mật ong")
                .description("Sườn heo mềm nướng mật ong thơm ngon")
                .price(130000D)
                .quantity(40)
                .imageUrl("https://res.cloudinary.com/kkskswrh/image/upload/v1784196819/suonbonuongmatong-1200x676_iuhegw.jpg")
                .status(true)
                .category(c1)
                .build();



        Food f3 = Food.builder()
                .name("Gà nướng nguyên con")
                .description("Gà nướng than hoa truyền thống")
                .price(250000D)
                .quantity(20)
                .imageUrl("https://res.cloudinary.com/kkskswrh/image/upload/v1784196861/hq720_hkxdxd.jpg")
                .status(true)
                .category(c1)
                .build();



        // =====================
        // FOOD - HẢI SẢN
        // =====================


        Food f4 = Food.builder()
                .name("Tôm nướng muối ớt")
                .description("Tôm tươi nướng muối ớt cay đậm vị")
                .price(120000D)
                .quantity(40)
                .imageUrl("https://res.cloudinary.com/kkskswrh/image/upload/v1784196952/cach-lam-tom-nuong-muoi-ot-ai-cung-ghien-800_j6knzp.jpg")
                .status(true)
                .category(c2)
                .build();



        Food f5 = Food.builder()
                .name("Mực nướng sa tế")
                .description("Mực tươi nướng sốt sa tế")
                .price(110000D)
                .quantity(35)
                .imageUrl("https://res.cloudinary.com/kkskswrh/image/upload/v1784196997/Rectangle_39_mwcqxe.png")
                .status(true)
                .category(c2)
                .build();



        // =====================
        // FOOD - RAU CỦ
        // =====================


        Food f6 = Food.builder()
                .name("Nấm kim châm nướng")
                .description("Nấm kim châm nướng giấy bạc")
                .price(50000D)
                .quantity(100)
                .imageUrl("https://res.cloudinary.com/kkskswrh/image/upload/v1784197170/47-nam-kim-cham-nuong-giay-bac_x5cg3e.jpg")
                .status(true)
                .category(c3)
                .build();



        Food f7 = Food.builder()
                .name("Bắp ngô nướng bơ")
                .description("Ngô nướng bơ thơm béo")
                .price(40000D)
                .quantity(80)
                .imageUrl("https://res.cloudinary.com/kkskswrh/image/upload/v1784197217/cach-lam-bap-nuong-bo-mon-khai-vi-667560242500_k6pn99.jpg")
                .status(true)
                .category(c3)
                .build();



        foodService.createFood(f1, null);
        foodService.createFood(f2, null);
        foodService.createFood(f3, null);
        foodService.createFood(f4, null);
        foodService.createFood(f5, null);
        foodService.createFood(f6, null);
        foodService.createFood(f7, null);
    }
}
