package com.firstofthekind.javaschoolproject.controller;


import com.firstofthekind.javaschoolproject.dto.SupplementDto;
import com.firstofthekind.javaschoolproject.dto.TariffDto;
import com.firstofthekind.javaschoolproject.exception.IncompatibleSupplementException;
import com.firstofthekind.javaschoolproject.service.ClientService;
import com.firstofthekind.javaschoolproject.service.ContractService;
import com.firstofthekind.javaschoolproject.service.SupplementService;
import com.firstofthekind.javaschoolproject.service.TariffService;
import com.firstofthekind.javaschoolproject.utils.Merge;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;

@Controller
@RequiredArgsConstructor
@Log4j2
public class TariffController {

    private final TariffService tariffService;
    private final ContractService contractService;
    private final ClientService clientService;
    private final SupplementService supplementService;

    @GetMapping("/tariffs")
    public String showTariff(ModelMap modelMap, HttpSession session) {
        session.removeAttribute("contract");
        session.removeAttribute("tariff");
        modelMap.put("tariffs", tariffService.getAll());
        return "tariffs";
    }

    @GetMapping("/tariffs/{id}")
    public String showTariffforClient(@PathVariable("id") long id,
                                      ModelMap modelMap, HttpSession session) {
        modelMap.put("tariffs", tariffService.getAll());
        session.removeAttribute("contract");
        session.setAttribute("client", clientService.getClientDto(id));
        return "tariffs";
    }

    @GetMapping("/tariffs/{clientId}/select/{tariffId}")
    public String addTariffClient(@PathVariable long tariffId,
                                  ModelMap map, HttpSession session) {
        log.info("tariff prowel");
        session.setAttribute("tariff", tariffService.getById(tariffId));
        return "redirect:/supplements";
    }

    @GetMapping("/tariffs/c/{contractId}")
    public String showChangeTariff(@PathVariable("contractId") long contractId,
                                   ModelMap modelMap, HttpSession session) {
        modelMap.put("tariffs", tariffService.getAll());
        session.removeAttribute("client");
        session.setAttribute("contract", contractService.getById(contractId));
        return "tariffs";
    }

    @GetMapping("/tariffs/c/{contractId}/select/{tariffId}")
    public String changeTariff(@PathVariable long contractId,
                               @PathVariable long tariffId,
                               ModelMap map, HttpSession session) {
        log.info("tariff prowel");
        session.setAttribute("contract", contractService.getById(contractId));
        session.setAttribute("tariff", tariffService.getById(tariffId));
        return "redirect:/supplements";
    }

    @GetMapping("/newtariff")
    public String showNewTariff(ModelMap modelMap) {
        return "newtariff";
    }

    @GetMapping("/tarifflist/{tariffid}/del")
    public String deleteTariff(@PathVariable("tariffid") long id, ModelMap modelMap) {
        tariffService.setDeleted(id, true);
        return "redirect:" + "/tarifflist";
    }

    @GetMapping("/tarifflist/{tariffid}/undel")
    public String restoreTariff(@PathVariable("tariffid") long id, ModelMap modelMap) {
        tariffService.setDeleted(id, false);
        return "redirect:" + "/tarifflist";
    }

    @GetMapping("/edittariff/{tariffid}")
    public String showEditTariff(@PathVariable("tariffid") long id, ModelMap modelMap) {
        modelMap.put("tariff", tariffService.getById(id));
        return "/edittariff";
    }

    @PostMapping("/edittariff/{tariffid}")
    public String editTariff(@PathVariable("tariffid") long id,
                             @ModelAttribute("editTariffDto") TariffDto editTariffDto,
                             ModelMap modelMap) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        tariffService.save(Merge.merge(tariffService.getById(id), editTariffDto));
        log.info("tariff with id " + editTariffDto.getId() + " was updated");
        return "redirect:" + "/tarifflist";
    }


    @PostMapping("/newtariff")
    public String newTarrif(@Valid @ModelAttribute("tariffDto") TariffDto tariffDto) {
        tariffService.save(tariffDto);
        return "redirect:" + "tarifflist?created";
    }

    @GetMapping("/tarifflist")
    public String showAllTariffs(ModelMap modelMap) {
        modelMap.put("tariffs", tariffService.getAll());
        return "tarifflist";
    }

    //Tariff' supplements editor

    @GetMapping("/edittariffsup/{tariffid}")
    public String showEditRelatedSupplement(@PathVariable("tariffid") long id, ModelMap modelMap) {
        SupplementDto supDto = supplementService.getById(id);
        TariffDto tariffDto = tariffService.getById(id);
        modelMap.put("tariff", tariffDto);
        modelMap.put("available", supplementService.getAllAvailableToTariff(id));
        modelMap.put("current", supplementService.getTariffSupplements(id));
        return "edittariffsup";
    }

    @PostMapping("/edittariffsup/{tariffid}/dep/{supid}")
    public String addDependent(@PathVariable("tariffid") long id,
                               @PathVariable("supid") long relId,
                               @ModelAttribute("editSupplementDto") SupplementDto editsupplementDto,
                               ModelMap modelMap) throws InvocationTargetException, IllegalAccessException, InstantiationException, IncompatibleSupplementException {
        tariffService.addSupplement(id, relId);
        log.info("supplement with id " + editsupplementDto.getId() + " was updated");
        return "redirect:" + "/edittariffsup/" + id;
    }

    @GetMapping("/edittariffsup/{tariffid}/dep/{supid}")
    public String delDependent(@PathVariable("tariffid") long id,
                               @PathVariable("supid") long relId,
                               @ModelAttribute("editSupplementDto") SupplementDto editsupplementDto,
                               ModelMap modelMap) throws InvocationTargetException, IllegalAccessException, InstantiationException, IncompatibleSupplementException {
        tariffService.delSupplement(id, relId);
        log.info("tariff with id " + editsupplementDto.getId() + " was updated");
        return "redirect:" + "/edittariffsup/" + id;
    }
}
