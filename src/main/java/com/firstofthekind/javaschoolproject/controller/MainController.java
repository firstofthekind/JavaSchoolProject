package com.firstofthekind.javaschoolproject.controller;


import com.firstofthekind.javaschoolproject.dto.ContractDto;
import com.firstofthekind.javaschoolproject.dto.RegDto;
import com.firstofthekind.javaschoolproject.entity.ContractEntity;
import com.firstofthekind.javaschoolproject.service.ContractService;
import com.firstofthekind.javaschoolproject.service.TariffService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private ContractService contractService;
    @Autowired
    private TariffService tariffService;

    @GetMapping("/main")
    public String main() {
        return "main";
    }

    @GetMapping("/tariffs")
    public String tariff(ModelMap modelMap){
        modelMap.put("tariffs", tariffService.getAllTariffs());
        return "tariffs";
    }

    @GetMapping("/tariffs?supplement")
    public String supplemments(ModelMap modelMap){
        modelMap.put("tariffs", tariffService.getAllTariffs());
        return "tariffs?supplement";
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
