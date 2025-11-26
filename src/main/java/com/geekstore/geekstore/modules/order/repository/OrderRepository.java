package com.geekstore.geekstore.modules.order.repository;

import com.geekstore.geekstore.modules.order.model.Order;
import com.geekstore.geekstore.modules.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Busca todos os pedidos do usuário logado
    List<Order> findByUser(User user);

}
