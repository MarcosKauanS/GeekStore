package com.geekstore.geekstore.modules.user.profile.controller;

import com.geekstore.geekstore.modules.user.profile.dto.PasswordUpdateDTO;
import com.geekstore.geekstore.modules.user.profile.dto.ProfileUpdateDTO;
import com.geekstore.geekstore.modules.user.profile.service.ProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public String showProfile(Model model,
                              @RequestParam(required = false) String success,
                              @RequestParam(required = false) String passwordSuccess,
                              @RequestParam(required = false) String passwordError) {

        model.addAttribute("user", profileService.getLoggedUser());
        model.addAttribute("orders", profileService.getUserOrderHistory());
        model.addAttribute("profileUpdateDTO", new ProfileUpdateDTO());
        model.addAttribute("passwordUpdateDTO", new PasswordUpdateDTO());

        if (success != null) model.addAttribute("successMessage", "Perfil atualizado com sucesso.");
        if (passwordSuccess != null) model.addAttribute("passwordSuccessMessage", "Senha alterada com sucesso.");
        if (passwordError != null) model.addAttribute("passwordErrorMessage", "Erro ao alterar senha. Confira os dados.");

        return "user/profile/profile";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute ProfileUpdateDTO dto, RedirectAttributes ra) {
        profileService.updateProfile(dto);
        ra.addAttribute("success", "true");
        return "redirect:/profile";
    }

    @PostMapping("/password")
    public String updatePassword(@ModelAttribute PasswordUpdateDTO dto, RedirectAttributes ra) {
        boolean updated = profileService.updatePassword(dto);
        if (updated) {
            ra.addAttribute("passwordSuccess", "true");
        } else {
            ra.addAttribute("passwordError", "true");
        }
        return "redirect:/profile";
    }
}
