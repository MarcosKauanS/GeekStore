package com.geekstore.geekstore.modules.auth.dto;

public record RegisterDTO(
        String name,
        String email,
        String password,
        String confirmPassword
) {}
