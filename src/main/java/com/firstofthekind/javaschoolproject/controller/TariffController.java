package com.firstofthekind.javaschoolproject.controller;


import com.firstofthekind.javaschoolproject.dto.TariffDto;
import com.firstofthekind.javaschoolproject.service.ContractService;
import com.firstofthekind.javaschoolproject.service.TariffService;
import com.firstofthekind.javaschoolproject.utils.Merge;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;

@Controller
@RequiredArgsConstructor
@Log4j2
public class TariffController {

    private final TariffService tariffService;
    private final ContractService contractService;

    @GetMapping("/tariffs")
    public String showTariff(ModelMap modelMap) {
        modelMap.put("tariffs", tariffService.getAll());
        return "tariffs";
    }

    @GetMapping("/newtariff")
    public String showNewTariff(ModelMap modelMap) {
        return "newtariff";
    }

    @GetMapping("/tarifflist/{tariffid}/del")
    public String deleteTariff(@PathVariable("tariffid") long id, ModelMap modelMap) {
        TariffDto tariffDto = tariffService.getById(id);
        tariffDto.setDeleted(true);
        tariffService.save(tariffDto);
        log.info("tariff with id " + tariffDto.getId() + " was dleeted");
        return "redirect:" + "/tarifflist";
    }

    @GetMapping("/edittariff/{tariffid}")
    public String showEditTariff(@PathVariable("tariffid") long id, ModelMap modelMap) {
        TariffDto tariffDto = tariffService.getById(id);
        modelMap.put("tariff", tariffDto);
        return "/edittariff";
    }

    @PostMapping("/edittariff/{tariffid}")
    public String editTariff(@PathVariable("tariffid") long id,
                             @ModelAttribute("editTariffDto") TariffDto editTariffDto,
                             ModelMap modelMap) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        TariffDto tariffDto = tariffService.getById(id);
        tariffDto = Merge.merge(tariffDto, editTariffDto);
        tariffService.save(tariffDto);
        log.info("tariff with id " + editTariffDto.getId() + " was updated");
        return "redirect:" + "/tarifflist";
    }

    @GetMapping("/tarifflist/{tariffid}/undel")
    public String restoreTariff(@PathVariable("tariffid") long id, ModelMap modelMap) {
        TariffDto tariffDto = tariffService.getById(id);
        tariffDto.setDeleted(false);
        tariffService.save(tariffDto);
        log.info("tariff with id " + tariffDto.getId() + " was dleeted");
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
}
