package com.geekstore.geekstore.modules.product.controller;

import com.geekstore.geekstore.modules.product.model.Product;
import com.geekstore.geekstore.modules.user.model.User; // Seu model de usuário
import com.geekstore.geekstore.modules.product.service.ProductService;
import com.geekstore.geekstore.modules.user.service.UserService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductController {

    private final ProductService productService;
    private final UserService userService; // serviço para buscar usuário

    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String redirectToProducts() {
        return "redirect:/products";
    }

    @GetMapping("/products")
    public String listProducts(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("products", productService.findAll());

        // Passa o usuário logado para a view
        if (userDetails != null) {
            User user = userService.findByEmail(userDetails.getUsername());
            model.addAttribute("user", user);
        }

        return "user/product/index"; // view: templates/product/index.html
    }

    @GetMapping("/products/{id}")
    public String showProduct(@PathVariable Long id, Model model,
            @AuthenticationPrincipal UserDetails userDetails) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto inválido: " + id));
        model.addAttribute("product", product);

        if (userDetails != null) {
            User user = userService.findByEmail(userDetails.getUsername());
            model.addAttribute("user", user);
        }

        return "user/product/show"; // view: templates/product/show.html
    }
}
