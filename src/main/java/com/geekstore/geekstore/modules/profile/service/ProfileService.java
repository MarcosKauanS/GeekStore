package com.geekstore.geekstore.modules.profile.service;

import com.geekstore.geekstore.modules.order.model.Order;
import com.geekstore.geekstore.modules.order.repository.OrderRepository;
import com.geekstore.geekstore.modules.profile.dto.PasswordUpdateDTO;
import com.geekstore.geekstore.modules.profile.dto.ProfileUpdateDTO;
import com.geekstore.geekstore.modules.user.model.User;
import com.geekstore.geekstore.modules.user.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletRequest request;

    public ProfileService(UserRepository userRepository,
            OrderRepository orderRepository,
            PasswordEncoder passwordEncoder,
            HttpServletRequest request) {

        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
        this.request = request;
    }

    // ✅ Pegar usuário logado
    public User findLoggedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();

        // ✅ Se já for sua entidade User, retorna direto
        if (principal instanceof User user) {
            return user;
        }

        // ✅ Se for UserDetails (Spring padrão), busca pelo username (email)
        if (principal instanceof UserDetails userDetails) {
            return userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException(
                            "Usuário não encontrado no banco: " + userDetails.getUsername()));
        }

        // ✅ Último fallback (se for String)
        if (principal instanceof String email) {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado no banco: " + email));
        }

        throw new RuntimeException("Tipo de usuário não reconhecido: " + principal.getClass().getName());
    }

    // ✅ Buscar histórico de pedidos
    public List<Order> findOrdersByUser() {
        User user = findLoggedUser();
        if (user == null)
            return List.of();

        return orderRepository.findByUser(user);
    }

    // ✅ Atualizar nome e email (SEM FAZER LOGOUT)
    public void update(ProfileUpdateDTO dto) {

        User user = findLoggedUser();

        if (user == null) {
            throw new RuntimeException("Usuário não autenticado.");
        }

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        userRepository.save(user);

        // ✅ Atualiza usuário dentro da sessão do Spring Security
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                user,
                currentAuth.getCredentials(),
                currentAuth.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    // ✅ Atualizar senha (FAZ LOGOUT AUTOMATICAMENTE)
    public boolean updatePassword(PasswordUpdateDTO dto) {

        User user = findLoggedUser();

        if (user == null)
            return false;

        // Confere senha atual
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            return false;
        }

        // Nova senha igual à antiga? (opcional mas profissional)
        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        // ✅ Encerra a sessão após alterar a senha (boa prática de segurança)
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();

        return true;
    }
}