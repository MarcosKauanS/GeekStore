package com.geekstore.geekstore.modules.user.profile.service;

import com.geekstore.geekstore.modules.user.model.User;
import com.geekstore.geekstore.modules.user.profile.dto.PasswordUpdateDTO;
import com.geekstore.geekstore.modules.user.profile.dto.ProfileUpdateDTO;
import com.geekstore.geekstore.modules.user.repository.UserRepository;
import com.geekstore.geekstore.modules.order.repository.OrderRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileService(UserRepository userRepository, OrderRepository orderRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getLoggedUser() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    String email;

    if (principal instanceof org.springframework.security.core.userdetails.User springUser) {
        email = springUser.getUsername(); // login = e-mail
    } else {
        email = principal.toString();
    }

    return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + email));
}


    public void updateProfile(ProfileUpdateDTO dto) {
        User user = getLoggedUser();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        userRepository.save(user);
    }

    public boolean updatePassword(PasswordUpdateDTO dto) {
        User user = getLoggedUser();

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            return false;
        }

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        return true;
    }

    public List<?> getUserOrderHistory() {
        User user = getLoggedUser();
        return orderRepository.findByUser(user); // Agora está 100% correto
    }
}
