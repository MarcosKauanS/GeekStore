package com.geekstore.geekstore.modules.order.controller;

import com.geekstore.geekstore.modules.order.model.Order;
import com.geekstore.geekstore.modules.order.service.OrderService;
import com.geekstore.geekstore.modules.user.model.User; // Import do User
import com.geekstore.geekstore.modules.user.service.UserService; // Import do UserService

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService; // 1. Injeção do UserService

    // 2. Atualização do Construtor
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    // Página de confirmação de pedido (após checkout)
    @GetMapping("/confirmation/{id}")
    public String orderConfirmation(@PathVariable Long id,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails) { // 3. Recebe o usuário logado

        Optional<Order> orderOpt = orderService.findById(id);

        if (orderOpt.isEmpty()) {
            return "redirect:/cart";
        }

        Order order = orderOpt.get();
        model.addAttribute("order", order);

        // 4. Lógica para passar o usuário para a View (Navbar)
        if (userDetails != null) {
            User user = userService.findByEmail(userDetails.getUsername());
            model.addAttribute("user", user);
        }

        return "user/order/confirmation";
    }
}