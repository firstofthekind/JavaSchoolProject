package com.firstofthekind.javaschoolproject.controller;


import com.firstofthekind.javaschoolproject.dto.ClientDto;
import com.firstofthekind.javaschoolproject.dto.ContractDto;
import com.firstofthekind.javaschoolproject.service.ClientService;
import com.firstofthekind.javaschoolproject.service.ContractService;
import com.firstofthekind.javaschoolproject.service.SupplementService;
import com.firstofthekind.javaschoolproject.service.TariffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.LinkedList;

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
        contractService.blockContractByAdmin(id, false);
        log.info("contract with id " + id + " was restored by admin");
        return "redirect:" + "/contractlist";
    }

    @GetMapping("/contractlist/{contractid}/del")
    public String blockContract(@PathVariable("contractid") long id, ModelMap modelMap) {
        contractService.blockContractByAdmin(id, true);
        log.info("contract with id " + id + " was blocked by admin");
        return "redirect:" + "/contractlist";
    }

    @GetMapping("/clientedit/{id}")
    public String showEditClient(@PathVariable("id") long id,
                                 ModelMap modelMap) {
        modelMap.put("currentClient", clientService.getClientDto(id));
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
        clientService.setStatus(id, false);
        return "redirect:" + "/userlist";
    }

    @GetMapping("/userlist/{clientid}/del")
    public String deleteClient(@PathVariable("clientid") long id, ModelMap modelMap) {
        clientService.setStatus(id, true);
        return "redirect:" + "/userlist";
    }

    @GetMapping("/clientprofile/{id}")
    public String showClientProfile(@PathVariable("id") long id,
                                    ModelMap modelMap) {
        modelMap.put("currentClient", clientService.getClientDto(id));
        modelMap.put("contracts", (LinkedList<ContractDto>) contractService.getAllByClientId(id));
        return "clientprofile";
    }

    @PostMapping("/finduser")
    public String findClientProfile(ModelMap modelMap,
                                    @ModelAttribute("contract") ContractDto contractDto) {
        try{
            return "redirect:" + "/clientprofile/" +
                    contractService.getByNumber(contractDto.getNumber()).getClient().getId();
        } catch (NullPointerException e){
            return "redirect:"+"/admin?notfound";
        }
    }

}
