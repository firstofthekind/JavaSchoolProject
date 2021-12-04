package com.firstofthekind.javaschoolproject.controller;

import com.firstofthekind.javaschoolproject.dto.ContractDto;
import com.firstofthekind.javaschoolproject.exception.ContractExistException;
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

@Controller
@RequiredArgsConstructor
@Log4j2
public class ContractController {
    private final ContractService contractService;
    private final ClientService clientService;
    private final TariffService tariffService;

    @GetMapping("/contracts/{clientId}")
    public String showClientContracts(@PathVariable long clientId, ModelMap modelMap) {
        modelMap.put("contractsByClientId " + clientId, contractService.getAllByClientId(clientId));
        return "/contracts/{clientId}";
    }

    @GetMapping("/editcontract/{contractId}")
    public String getContract(@PathVariable long contractId, ModelMap modelMap) {
        modelMap.put("contract", contractService.getEntityById(contractId));
        return "/editcontract";
    }

    @GetMapping("/editcontract/n")
    public String editContractNumber(@ModelAttribute("contractDto") ContractDto contractDto,
                                     ModelMap modelMap) {
        try {
            contractService.editContractNum(contractDto);
        } catch (ContractExistException e){
            return "redirect:"+"/editcontract/"+contractDto.getId()+"?mt1";
        }
        log.info("contract number with id " + contractDto.getId()
                + " changed to " + contractDto.getNumber());

        return "/main";
    }

    @GetMapping("/profile/{contractid}/undel")
    public String unblockClientByClient(@PathVariable("contractid") long id,
                                        ModelMap modelMap) {
        contractService.blockContractByClient(id, false);
        log.info("contract with id " + id + " was restored by client");
        return "redirect:" + "/profile";
    }

    @GetMapping("/profile/{contractid}/del")
    public String blockContractByClient(@PathVariable("contractid") long id,
                                        ModelMap modelMap) {
        contractService.blockContractByClient(id, true);
        log.info("contract with id " + id + " was blocked by client");
        return "redirect:" + "/profile";
    }
}
