package com.geekstore.geekstore.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geekstore.geekstore.modules.cart.model.CartItem;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CartCookieUtil {

    private static final String CART_COOKIE_NAME = "CART";
    private static final int MAX_AGE = 60 * 60 * 24 * 7; // 7 dias

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Recupera o carrinho a partir do cookie
     */
    public static List<CartItem> getCart(HttpServletRequest request) {
        try {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (CART_COOKIE_NAME.equals(cookie.getName())) {
                        String decoded = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                        return mapper.readValue(decoded, new TypeReference<List<CartItem>>() {});
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    /**
     * Salva o carrinho no cookie
     */
    public static void saveCart(HttpServletResponse response, List<CartItem> cart) {
        try {
            String json = mapper.writeValueAsString(cart);
            String encoded = URLEncoder.encode(json, StandardCharsets.UTF_8);

            Cookie cookie = new Cookie(CART_COOKIE_NAME, encoded);
            cookie.setPath("/");
            cookie.setMaxAge(MAX_AGE);
            cookie.setHttpOnly(true);

            response.addCookie(cookie);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Limpa o cookie do carrinho
     */
    public static void clear(HttpServletResponse response) {
        Cookie cookie = new Cookie(CART_COOKIE_NAME, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }
}
