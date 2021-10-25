package com.firstofthekind.javaschoolproject.controller;

import com.firstofthekind.javaschoolproject.dto.ContractDto;
import com.firstofthekind.javaschoolproject.dto.TariffDto;
import com.firstofthekind.javaschoolproject.service.ClientService;
import com.firstofthekind.javaschoolproject.service.ContractService;
import com.firstofthekind.javaschoolproject.service.TariffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ContractController {
    private final ContractService contractService;
    private final ClientService clientService;
    private final TariffService tariffService;

    @GetMapping("/contracts/{clientId}")
    public String showClientContracts(@PathVariable long clientId, ModelMap modelMap) {
        modelMap.put("contractsByClientId "+clientId, contractService.getAllByClientId(clientId));
        return "/contracts/{clientId}";
    }

    @GetMapping("/editcontract/{contractId}")
    public String getContract(@PathVariable long contractId, ModelMap modelMap) {
        modelMap.put("contract", contractService.getEntityById(contractId));
        return "/editcontract";
    }
}
