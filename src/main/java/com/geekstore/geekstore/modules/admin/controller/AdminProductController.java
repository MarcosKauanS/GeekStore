package com.geekstore.geekstore.modules.admin.controller;

import com.geekstore.geekstore.modules.product.model.Product;
import com.geekstore.geekstore.modules.category.model.Category;
import com.geekstore.geekstore.modules.product.service.ProductService;
import com.geekstore.geekstore.modules.category.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public AdminProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    // ===== LISTAGEM DE PRODUTOS =====
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        model.addAttribute("categories", categoryService.findAll()); // categorias para o modal
        return "admin/product/index";
    }

    // ===== CRIAR PRODUTO =====
    @PostMapping
    public String saveProduct(
            @ModelAttribute("product") Product product,
            @RequestParam(value = "image", required = false) MultipartFile fileImage,
            @RequestParam("category.id") Long categoryId, // recebe o id da categoria
            Model model) {
        try {
            // 1️⃣ Vincula a categoria ao produto
            Category category = categoryService.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Categoria inválida: " + categoryId));
            product.setCategory(category);

            // 2️⃣ Salva a imagem se houver
            if (fileImage != null && !fileImage.isEmpty()) {
                String imageUrl = productService.saveImageUrl(fileImage);
                product.setImageUrl(imageUrl);
            }

            // 3️⃣ Salva o produto
            productService.save(product);
            return "redirect:/admin/products";

        } catch (IOException | SecurityException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Erro ao salvar produto/imagem: " + e.getMessage());
            return "redirect:/admin/products";
        }
    }

    // ===== ATUALIZAR PRODUTO =====
    @PostMapping("/edit/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @ModelAttribute("product") Product updatedProduct,
            @RequestParam(value = "image", required = false) MultipartFile fileImage,
            @RequestParam("category.id") Long categoryId,
            Model model) {
        try {
            // 1️⃣ Busca o produto existente
            Product existingProduct = productService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Produto inválido: " + id));

            // 2️⃣ Atualiza campos básicos
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setPrice(updatedProduct.getPrice());

            // 3️⃣ Atualiza a categoria
            Category category = categoryService.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Categoria inválida: " + categoryId));
            existingProduct.setCategory(category);

            // 4️⃣ Atualiza a imagem se houver
            if (fileImage != null && !fileImage.isEmpty()) {
                String imageUrl = productService.saveImageUrl(fileImage);
                existingProduct.setImageUrl(imageUrl);
            }

            // 5️⃣ Salva novamente
            productService.save(existingProduct);
            return "redirect:/admin/products";

        } catch (IOException | SecurityException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Erro ao atualizar produto/imagem: " + e.getMessage());
            return "redirect:/admin/products";
        }
    }

    // ===== DELETAR PRODUTO =====
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/admin/products";
    }
}
