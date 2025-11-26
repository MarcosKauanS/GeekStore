package com.geekstore.geekstore.modules.product.controller;

import com.geekstore.geekstore.modules.product.model.Product;
import com.geekstore.geekstore.modules.product.service.ProductService;
import com.geekstore.geekstore.modules.user.model.User;
import com.geekstore.geekstore.modules.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    // Redireciona a raiz para a página de produtos
    @GetMapping("/")
    public String redirectToProducts() {
        return "redirect:/products";
    }

    // Listagem de produtos (com suporte a busca)
    @GetMapping("/products")
    public String listProducts(@RequestParam(value = "keyword", required = false) String keyword,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails) {

        List<Product> products;

        // Lógica de Busca: Se houver keyword, filtra; senão, traz tudo.
        if (keyword != null && !keyword.trim().isEmpty()) {
            products = productService.search(keyword);
            model.addAttribute("keyword", keyword); // Mantém o texto no input da view
        } else {
            products = productService.findAll();
        }

        model.addAttribute("products", products);

        // Passa o usuário logado para a view (para a Navbar funcionar)
        if (userDetails != null) {
            User user = userService.findByEmail(userDetails.getUsername());
            model.addAttribute("user", user);
        }

        return "user/product/index"; // view: templates/user/product/index.html
    }

    // Detalhes de um produto específico
    @GetMapping("/products/{id}")
    public String showProduct(@PathVariable Long id,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails) {

        Product product = productService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto inválido: " + id));

        model.addAttribute("product", product);

        // Passa o usuário logado para a view (para a Navbar funcionar)
        if (userDetails != null) {
            User user = userService.findByEmail(userDetails.getUsername());
            model.addAttribute("user", user);
        }

        return "user/product/show"; // view: templates/user/product/show.html
    }
}