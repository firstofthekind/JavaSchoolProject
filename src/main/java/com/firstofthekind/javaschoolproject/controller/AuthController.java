package com.firstofthekind.javaschoolproject.controller;

import com.firstofthekind.javaschoolproject.entity.ClientEntity;
import com.firstofthekind.javaschoolproject.repository.ClientRepository;
import com.firstofthekind.javaschoolproject.repository.RoleRepository;
import com.firstofthekind.javaschoolproject.dto.RegDto;
import com.firstofthekind.javaschoolproject.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/registration")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> registration(@Valid @ModelAttribute("regDto") RegDto regDto) {

        return clientService.registerClient(regDto);
    }

}
