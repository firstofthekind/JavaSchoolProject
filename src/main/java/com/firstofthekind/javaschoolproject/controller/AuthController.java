package com.firstofthekind.javaschoolproject.controller;

import com.firstofthekind.javaschoolproject.dto.RegDto;
import com.firstofthekind.javaschoolproject.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Log4j2
public class AuthController {

    private final ClientService clientService;

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
    public String registration(Model model, @Valid @ModelAttribute("regDto") RegDto regDto, BindingResult result) {
        if (result.hasErrors()) {
            log.info("ERROR PROWEl");
            return "registration";
        } else {
            clientService.registerClient(regDto);
            return "redirect:/login";
        }
    }

}
