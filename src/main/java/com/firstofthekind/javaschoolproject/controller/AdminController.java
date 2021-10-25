package com.firstofthekind.javaschoolproject.controller;


import com.firstofthekind.javaschoolproject.dto.ClientDto;
import com.firstofthekind.javaschoolproject.dto.ContractDto;
import com.firstofthekind.javaschoolproject.dto.TariffDto;
import com.firstofthekind.javaschoolproject.service.ClientService;
import com.firstofthekind.javaschoolproject.service.ContractService;
import com.firstofthekind.javaschoolproject.service.SupplementService;
import com.firstofthekind.javaschoolproject.service.TariffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@Log4j2
public class AdminController {
    private final ContractService contractService;
    private final TariffService tariffService;
    private final ClientService clientService;
    private final SupplementService supplementService;

    @GetMapping("/admin")
    public String showAdmin() {
        return "admin";
    }

    @GetMapping("/userlist")
    public String showClients(ModelMap modelMap) {
        modelMap.put("clients", clientService.getAll());
        return "userlist";
    }

    @GetMapping("/contractlist")
    public String showContracts(ModelMap modelMap) {
        modelMap.put("contracts", contractService.getAll());
        return "contractlist";
    }
    @GetMapping("/contractlist/{contractid}/undel")
    public String unblockClient(@PathVariable("contractid") long id, ModelMap modelMap) {
        contractService.blockContract(id,false);
        log.info("contract with id " + id + " was restored");
        return "redirect:" + "/contractlist";
    }

    @GetMapping("/contractlist/{contractid}/del")
    public String blockContract(@PathVariable("contractid") long id, ModelMap modelMap) {
        contractService.blockContract(id,true);
        log.info("contract with id " + id + " was restored");
        return "redirect:" + "/contractlist";
    }

    @GetMapping("/clientedit/{id}")
    public String showEditClient(@PathVariable("id") long id,
                                 ModelMap modelMap) {
        ClientDto clientDto1 = clientService.getClientDto(id);
        modelMap.put("currentClient", clientDto1);
        return "/clientedit";
    }

    @PostMapping("/clientedit")
    public String editClient(ModelMap modelMap,
                             @ModelAttribute("editClientDto") ClientDto clientDto) {
        clientService.updateClient(clientDto);
        return "/main";
    }

    @GetMapping("/userlist/{clientid}/undel")
    public String restoreClient(@PathVariable("clientid") long id, ModelMap modelMap) {
        ClientDto clientDto = clientService.getClientDto(id);
        clientDto.setDeleted(false);
        clientService.updateClient(clientDto);
        log.info("tariff with id " + clientDto.getId() + " was restored");
        return "redirect:" + "/userlist";
    }

    @GetMapping("/userlist/{clientid}/del")
    public String deleteClient(@PathVariable("clientid") long id, ModelMap modelMap) {
        ClientDto clientDto = clientService.getClientDto(id);
        clientDto.setDeleted(true);
        clientService.updateClient(clientDto);
        log.info("client with id " + clientDto.getId() + " was dleeted");
        return "redirect:" + "/userlist";
    }

    @GetMapping("/clientprofile/{id}")
    public String showClientProfile(@PathVariable("id") long id,
                                    ModelMap modelMap) {
        ClientDto clientDto = clientService.getClientDto(id);
        modelMap.put("currentClient", clientDto);
        HashSet<ContractDto> contractDtos = (HashSet<ContractDto>) contractService.getAllByClientId(id);
        modelMap.put("contracts", contractDtos);
        return "clientprofile";
    }
/*
    @GetMapping("/profileedit/{id}")
    public String showEditClientProfile(@PathVariable("id") long id,
                                  ModelMap modelMap) {
        ClientDto clientDto1 = clientService.getClientDto(id);
        modelMap.put("currentClient", clientDto1);
        return "/profileedit";
    }
    @PostMapping("/profileedit/{id}")
    public String editClientProfile(ModelMap modelMap,
                              @ModelAttribute("editClientDto") ClientDto clientDto) {
        clientService.updateClient(clientDto);
        return "/main";
    }*/

    @PostMapping("/finduser")
    public String findClientProfile(ModelMap modelMap,
                                    @ModelAttribute("contract") ContractDto contractDto) {
        return "redirect:"+"/clientprofile/"+contractService.getByNumber(contractDto.getNumber()).getClient().getId();
    }
/*

    @GetMapping("/supplementlist")
    public String showContracts(ModelMap modelMap) {
        modelMap.put("tariffs", contractService.getAll());
        return "tariffs";
    }
    @GetMapping("/supplementlist")
    public String showSupplements(ModelMap modelMap) {
        modelMap.put("supplements", supplementService.getAll());
        return "supplementlist";
    }*/

}
