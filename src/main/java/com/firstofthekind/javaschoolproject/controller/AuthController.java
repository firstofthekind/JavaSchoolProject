package com.firstofthekind.javaschoolproject.controller;

import com.firstofthekind.javaschoolproject.dto.RegDto;
import com.firstofthekind.javaschoolproject.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private ClientService clientService;

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
    @ResponseStatus(value = HttpStatus.OK)
    public String registration(@Valid @ModelAttribute("regDto") RegDto regDto) {
        clientService.registerClient(regDto);
        return "login";
    }

}
