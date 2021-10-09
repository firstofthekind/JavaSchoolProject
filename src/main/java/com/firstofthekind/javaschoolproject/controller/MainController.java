package com.firstofthekind.javaschoolproject.controller;


import com.firstofthekind.javaschoolproject.dto.RegDto;
import com.firstofthekind.javaschoolproject.dto.TariffDto;
import com.firstofthekind.javaschoolproject.service.ContractService;
import com.firstofthekind.javaschoolproject.service.SupplementService;
import com.firstofthekind.javaschoolproject.service.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

@Controller
public class MainController {
    @Autowired
    private ContractService contractService;
    @Autowired
    private TariffService tariffService;
    @Autowired
    private SupplementService supplementService;

    private ServletUriComponentsBuilder servletUriComponentsBuilder;

    @GetMapping("/main")
    public String main() {
        return "main";
    }

    @GetMapping("/tariffs")
    public String tariff(ModelMap modelMap) {
        modelMap.put("tariffs", tariffService.getAll());
        return "tariffs";
    }
    @PostMapping("/tariffs")
    public String registration(@Valid @ModelAttribute("tariffDto") TariffDto tariffDto) {
        tariffService.save(tariffDto);
        return "redirect:"+"tariffs?created";
    }

/*
    @GetMapping(value = {"", "/", "main"})
    public Iterable<ContractDto> getContracts() {
        return contractService.getAllContracts();
    }

    @PostMapping("/cart")
    public ResponseEntity<ContractDto> create(@Valid @ModelAttribute("contractDto")
                                                          ContractDto contractDto) {
        ContractEntity formDtos = contractDto.;
        validateProductsExistence(formDtos);
        // create order logic
        // populate order with products

        order.setOrderProducts(orderProducts);
        this.orderService.update(order);

        String uri = ServletUriComponentsBuilder
                .fromCurrentServletMapping()
                .path("/orders/{id}")
                .buildAndExpand(order.getId())
                .toString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", uri);

        return new ResponseEntity<>(order, headers, HttpStatus.CREATED);
    }*/
}
