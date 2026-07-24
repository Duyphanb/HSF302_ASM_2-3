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
    public void run(String... args) {

        if (foodService.getAllFood().size() > 0) {
            return;
        }

        Category c1 = Category.builder()
                .name("Thit")
                .description("Cac mon thit nuong BBQ")
                .build();

        Category c2 = Category.builder()
                .name("Hai san")
                .description("Cac mon hai san nuong")
                .build();

        Category c3 = Category.builder()
                .name("Rau cu")
                .description("Cac loai rau cu nuong")
                .build();

        cateSercive.createCategory(c1);
        cateSercive.createCategory(c2);
        cateSercive.createCategory(c3);

        Food f1 = Food.builder()
                .name("Ba chi bo My nuong BBQ")
                .description("<p><strong>Premium BBQ beef</strong> with a rich house sauce.</p><ul><li><em>Thin sliced</em> for quick grilling</li><li><span style='color:#d9480f;'>Best seller</span> for BBQ nights</li></ul>")
                .price(150000D)
                .quantity(50)
                .imageUrl("https://res.cloudinary.com/kkskswrh/image/upload/v1784196412/bo_hmpzwj.jpg")
                .status(true)
                .category(c1)
                .build();

        Food f2 = Food.builder()
                .name("Suon heo nuong mat ong")
                .description("<p><strong>Honey glazed pork ribs</strong> with a soft texture and sweet finish.</p><p><span style='font-size:1.1rem;'>Great for sharing.</span></p>")
                .price(130000D)
                .quantity(40)
                .imageUrl("https://res.cloudinary.com/kkskswrh/image/upload/v1784196819/suonbonuongmatong-1200x676_iuhegw.jpg")
                .status(true)
                .category(c1)
                .build();

        Food f3 = Food.builder()
                .name("Ga nuong nguyen con")
                .description("<p><strong>Charcoal grilled chicken</strong> with classic seasoning.</p><p><em>Smoky aroma</em> and juicy meat in every serving.</p>")
                .price(250000D)
                .quantity(20)
                .imageUrl("https://res.cloudinary.com/kkskswrh/image/upload/v1784196861/hq720_hkxdxd.jpg")
                .status(true)
                .category(c1)
                .build();

        Food f4 = Food.builder()
                .name("Tom nuong muoi ot")
                .description("<p><strong>Grilled shrimp</strong> with chili salt seasoning.</p><ul><li>Fresh seafood</li><li><span style='color:#c92a2a;'>Spicy</span> and bold flavor</li></ul>")
                .price(120000D)
                .quantity(40)
                .imageUrl("https://res.cloudinary.com/kkskswrh/image/upload/v1784196952/cach-lam-tom-nuong-muoi-ot-ai-cung-ghien-800_j6knzp.jpg")
                .status(true)
                .category(c2)
                .build();

        Food f5 = Food.builder()
                .name("Muc nuong sa te")
                .description("<p><strong>Fresh squid</strong> brushed with satay sauce.</p><p><span style='font-size:1.2rem;'>Hot pick</span> for spicy lovers.</p>")
                .price(110000D)
                .quantity(35)
                .imageUrl("https://res.cloudinary.com/kkskswrh/image/upload/v1784196997/Rectangle_39_mwcqxe.png")
                .status(true)
                .category(c2)
                .build();

        Food f6 = Food.builder()
                .name("Nam kim cham nuong")
                .description("<p><strong>Enoki mushrooms</strong> grilled in foil.</p><p><em>Light, simple, and easy to pair</em> with meat dishes.</p>")
                .price(50000D)
                .quantity(100)
                .imageUrl("https://res.cloudinary.com/kkskswrh/image/upload/v1784197170/47-nam-kim-cham-nuong-giay-bac_x5cg3e.jpg")
                .status(true)
                .category(c3)
                .build();

        Food f7 = Food.builder()
                .name("Bap ngo nuong bo")
                .description("<p><strong>Butter grilled corn</strong> with a sweet and creamy taste.</p><p><span style='color:#f08c00;'>Kid-friendly</span> side dish.</p>")
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
