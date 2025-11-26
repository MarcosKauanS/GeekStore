package com.geekstore.geekstore.modules.cart.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geekstore.geekstore.modules.order.model.Order;
import com.geekstore.geekstore.modules.order.model.OrderItem;
import com.geekstore.geekstore.modules.product.model.Product;
import com.geekstore.geekstore.modules.stock.model.Stock;
import com.geekstore.geekstore.modules.user.model.User;
import com.geekstore.geekstore.modules.order.service.OrderService;
import com.geekstore.geekstore.modules.product.service.ProductService;
import com.geekstore.geekstore.modules.stock.service.StockService;
import com.geekstore.geekstore.modules.user.service.UserService;
import com.geekstore.geekstore.modules.cart.model.CartItem;
import com.geekstore.geekstore.modules.cart.model.CartProduct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final ProductService productService;
    private final StockService stockService;
    private final OrderService orderService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public CartController(ProductService productService,
            StockService stockService,
            OrderService orderService,
            UserService userService) {
        this.productService = productService;
        this.stockService = stockService;
        this.orderService = orderService;
        this.userService = userService;
        this.objectMapper = new ObjectMapper();
    }

    // ===================== MÉTODOS AUXILIARES =====================
    @SuppressWarnings("unchecked")
    private List<CartItem> getCartFromSession(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private void saveCartToSession(HttpSession session, List<CartItem> cart) {
        session.setAttribute("cart", cart);
    }

    private void saveCartToCookie(HttpServletResponse response, List<CartItem> cart) {
        try {
            String json = objectMapper.writeValueAsString(cart);
            String encoded = URLEncoder.encode(json, StandardCharsets.UTF_8);
            Cookie cookie = new Cookie("cart", encoded);
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60); // 7 dias
            response.addCookie(cookie);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private List<CartItem> getCartFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("cart".equals(cookie.getName())) {
                    try {
                        String decoded = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                        return objectMapper.readValue(decoded, new TypeReference<List<CartItem>>() {
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    private void syncSessionAndCookie(HttpSession session, HttpServletResponse response, List<CartItem> cart) {
        saveCartToSession(session, cart);
        saveCartToCookie(response, cart);
    }

    // ===================== ADICIONAR AO CARRINHO =====================
    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId,
            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
            HttpServletRequest request,
            HttpServletResponse response) {

        HttpSession session = request.getSession();
        List<CartItem> cart = getCartFromSession(session);

        boolean found = false;
        for (CartItem item : cart) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity); // Aqui SOMA (correto para adicionar)
                found = true;
                break;
            }
        }

        if (!found) {
            cart.add(new CartItem(productId, quantity));
        }

        syncSessionAndCookie(session, response, cart);
        return "redirect:/cart";
    }

    // ===================== ATUALIZAR QUANTIDADE (NOVO MÉTODO)
    // =====================
    @PostMapping("/update")
    public String updateCart(@RequestParam("productId") Long productId,
            @RequestParam("quantity") int quantity,
            HttpServletRequest request,
            HttpServletResponse response) {

        // Se quantidade for 0 ou menor, remove o item
        if (quantity <= 0) {
            return removeFromCart(productId, request, response);
        }

        HttpSession session = request.getSession();
        List<CartItem> cart = getCartFromSession(session);

        for (CartItem item : cart) {
            if (item.getProductId().equals(productId)) {
                // Aqui SUBSTITUI o valor (lógica de atualização)
                item.setQuantity(quantity);
                break;
            }
        }

        syncSessionAndCookie(session, response, cart);
        return "redirect:/cart";
    }

    // ===================== REMOVER DO CARRINHO =====================
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("productId") Long productId,
            HttpServletRequest request,
            HttpServletResponse response) {

        HttpSession session = request.getSession();
        List<CartItem> cart = getCartFromSession(session);

        cart.removeIf(item -> item.getProductId().equals(productId));

        syncSessionAndCookie(session, response, cart);

        return "redirect:/cart";
    }

    // ===================== EXIBIR CARRINHO =====================
    @GetMapping
    public String viewCart(HttpServletRequest request, Model model,
            @AuthenticationPrincipal UserDetails userDetails) {
        HttpSession session = request.getSession();
        List<CartItem> cart = getCartFromSession(session);

        if (cart.isEmpty()) {
            cart = getCartFromCookie(request);
            saveCartToSession(session, cart);
        }

        List<CartProduct> cartProducts = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (CartItem item : cart) {
            Optional<Product> productOpt = productService.findById(item.getProductId());
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                subtotal = subtotal.add(total);
                cartProducts.add(new CartProduct(product, item.getQuantity()));
            }
        }

        if (userDetails != null) {
            User user = userService.findByEmail(userDetails.getUsername());
            model.addAttribute("user", user);
        }

        model.addAttribute("cartItems", cartProducts);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("total", subtotal); // frete grátis por enquanto
        return "user/product/cart";
    }

    // ===================== FINALIZAR COMPRA =====================
    @GetMapping("/checkout")
    public String checkout(HttpServletRequest request, HttpServletResponse response, Model model,
            @AuthenticationPrincipal UserDetails userDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }

        User user = userService.findByEmail(auth.getName());
        HttpSession session = request.getSession();
        List<CartItem> cart = getCartFromSession(session);

        if (cart.isEmpty()) {
            cart = getCartFromCookie(request);
            if (cart.isEmpty()) {
                model.addAttribute("errorMessage", "Seu carrinho está vazio.");
                return "user/product/cart";
            }
        }

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        try {
            for (CartItem item : cart) {
                Product product = productService.findById(item.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("Produto inválido: " + item.getProductId()));

                // Verifica estoque
                Stock stock = stockService.findByProduct(product)
                        .orElseThrow(() -> new IllegalArgumentException("Produto sem estoque: " + product.getName()));

                if (item.getQuantity() > stock.getQuantity()) {
                    throw new IllegalArgumentException(
                            "Quantidade solicitada maior que o estoque disponível para: " + product.getName());
                }

                // Criar item de pedido
                OrderItem orderItem = new OrderItem(null, product, item.getQuantity(), product.getPrice());
                orderItems.add(orderItem);

                // Atualizar estoque
                stock.setQuantity(stock.getQuantity() - item.getQuantity());
                stockService.save(stock);

                totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }

            // Criar pedido
            Order order = new Order();
            order.setUser(user);
            order.setStatus("PENDING");
            order.setTotalAmount(totalAmount);
            order.setItems(orderItems);
            // Vincula cada item ao pedido
            orderItems.forEach(i -> i.setOrder(order));

            orderService.save(order);

            // Limpar carrinho
            cart.clear();
            syncSessionAndCookie(session, response, cart);

            return "redirect:/orders/confirmation/" + order.getId();

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return viewCart(request, model, userDetails); // retorna ao carrinho exibindo a mensagem
        }
    }
}