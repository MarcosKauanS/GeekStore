// package com.geekstore.geekstore.config.seeder;

// import com.geekstore.geekstore.modules.product.model.Product;
// import com.geekstore.geekstore.modules.stock.model.Stock;
// import com.geekstore.geekstore.modules.category.model.Category;
// import com.geekstore.geekstore.modules.category.repository.CategoryRepository;
// import com.geekstore.geekstore.modules.product.repository.ProductRepository;
// import com.geekstore.geekstore.modules.stock.repository.StockRepository;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Configuration;

// import java.math.BigDecimal;
// import java.util.*;

// @Configuration
// public class ProductSeeder implements CommandLineRunner {

//     private final ProductRepository productRepository;
//     private final CategoryRepository categoryRepository;
//     private final StockRepository stockRepository;

//     public ProductSeeder(ProductRepository productRepository,
//                          CategoryRepository categoryRepository,
//                          StockRepository stockRepository) {
//         this.productRepository = productRepository;
//         this.categoryRepository = categoryRepository;
//         this.stockRepository = stockRepository;
//     }

//     @Override
//     public void run(String... args) {

//         if (productRepository.count() > 0) {
//             System.out.println("✅ Produtos já foram inseridos.");
//             return;
//         }

//         List<String> productNames = List.of(
//                 "Camiseta Marvel", "Camiseta Naruto", "Action Figure Goku",
//                 "Caneca Gamer", "Poster Batman", "Mousepad RGB",
//                 "Camiseta Star Wars", "Action Figure Batman",
//                 "Caneca One Piece", "Poster Attack on Titan",
//                 "Chaveiro Zelda", "Camiseta Dragon Ball",
//                 "Boneco Pikachu", "Caneca Hogwarts",
//                 "Poster Deadpool", "Headset Gamer",
//                 "Mouse Gamer", "Teclado Mecânico",
//                 "Camiseta Pokémon", "Poster Cyberpunk",
//                 "Boneco Kratos", "Caneca PlayStation",
//                 "Action Figure Spiderman",
//                 "Camiseta Resident Evil",
//                 "Action Figure Levi Ackerman"
//         );

//         List<String> categoryNames = List.of(
//                 "Camisetas Geek",
//                 "Action Figures",
//                 "Canecas",
//                 "Posters",
//                 "Acessórios"
//         );

//         List<String> images = List.of(
//                 "images/produto1.jpg",
//                 "images/produto2.jpg",
//                 "images/produto3.jpg"
//         );

//         Random random = new Random();

//         /* =====================
//            CRIA AS CATEGORIAS 1 VEZ
//         ====================== */

//         Map<String, Category> categoryMap = new HashMap<>();

//         for (String categoryName : categoryNames) {
//             Optional<Category> existing = categoryRepository.findByName(categoryName);

//             Category category = existing.orElseGet(() -> {
//                 Category newCategory = new Category();
//                 newCategory.setName(categoryName);
//                 return categoryRepository.save(newCategory);
//             });

//             categoryMap.put(categoryName, category);
//         }

//         /* =====================
//            CRIA OS PRODUTOS
//         ====================== */

//         for (int i = 1; i <= 9; i++) {

//             Product product = new Product();
//             Stock stock = new Stock();

//             String name = productNames.get(random.nextInt(productNames.size())) + " #" + i;
//             String categoryName = categoryNames.get(random.nextInt(categoryNames.size()));

//             product.setName(name);
//             product.setDescription("Produto geek premium: " + name);
//             product.setImageUrl(images.get(random.nextInt(images.size())));
//             product.setPrice(
//                     BigDecimal.valueOf(29.90 + random.nextInt(200))
//             );

//             // Relacionamento
//             product.setCategory(categoryMap.get(categoryName));

//             stock.setQuantity(random.nextInt(40) + 10);
//             stock.setProduct(product); // relacionamento

//             productRepository.save(product);
//             stockRepository.save(stock);
//         }

//         System.out.println("✅ 50 produtos Geek inseridos com sucesso!");
//     }
// }
