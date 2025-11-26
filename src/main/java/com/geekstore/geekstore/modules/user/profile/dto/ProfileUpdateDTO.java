package com.geekstore.geekstore.modules.user.profile.dto;

public class ProfileUpdateDTO {

    private String name;
    private String email;

    public ProfileUpdateDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
