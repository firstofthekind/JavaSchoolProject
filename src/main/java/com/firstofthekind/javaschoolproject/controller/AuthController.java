package com.firstofthekind.javaschoolproject.controller;

import com.firstofthekind.javaschoolproject.dto.RegDto;
import com.firstofthekind.javaschoolproject.service.ClientService;
import com.firstofthekind.javaschoolproject.service.TariffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Log4j2
public class AuthController {

    private final ClientService clientService;
    private final TariffService tariffService;

    @GetMapping("/main")
    public String main() {
        return "main";
    }

    @GetMapping(value = "/registration")
    public String registration(Model model) {
        model.addAttribute("regDto", new RegDto());
        return "registration";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/registration")
    public String registration(Model model,
                               @Valid @ModelAttribute("regDto") RegDto regDto,
                               BindingResult result) {
        if (result.hasErrors()) {
            log.info("Registration error");
            return "registration";
        } else {
            clientService.registerClient(regDto);
            return "redirect:/login?reg";
        }
    }

}
