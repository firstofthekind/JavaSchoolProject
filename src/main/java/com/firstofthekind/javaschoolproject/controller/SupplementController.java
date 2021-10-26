package com.firstofthekind.javaschoolproject.controller;


import com.firstofthekind.javaschoolproject.dto.SupplementDto;
import com.firstofthekind.javaschoolproject.dto.TariffDto;
import com.firstofthekind.javaschoolproject.service.SupplementService;
import com.firstofthekind.javaschoolproject.utils.Merge;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;

@Controller
@RequiredArgsConstructor
@Log4j2
public class SupplementController {
    private final SupplementService supplementService;


    @GetMapping("/supplementlist")
    public String showAllSupplements(ModelMap modelMap) {
        modelMap.put("supplements", supplementService.getAll());
        return "supplementlist";
    }

    @GetMapping("/supplements/{supplementId}")
    public String showAvailable(ModelMap modelMap) {
        modelMap.put("supplements", supplementService.getAll());
        return "supplements";
    }

    @GetMapping("/supplementlist/{supplementid}/del")
    public String deleteSupplement(@PathVariable("supplementid") long id,
                                   ModelMap modelMap) {
        supplementService.setDeleted(id, true);
        return "redirect:" + "/supplementlist";
    }

    @GetMapping("/supplementlist/{supid}/undel")
    public String restoreSupplement(@PathVariable("supid") long id,
                                    ModelMap modelMap) {
        supplementService.setDeleted(id, false);
        return "redirect:" + "/supplementlist";
    }

    @GetMapping("/editsupplement/{supid}")
    public String showEditSupplement(@PathVariable("supid") long id, ModelMap modelMap) {
        SupplementDto supDto = supplementService.getById(id);
        modelMap.put("supplement", supDto);
        return "/editsupplement";
    }

    @PostMapping("/editsupplement/{supid}")
    public String editSupplement(@PathVariable("supid") long id,
                                 @ModelAttribute("editSupplementDto") SupplementDto editsupplementDto,
                                 ModelMap modelMap) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        supplementService.save(Merge.merge(supplementService.getById(id), editsupplementDto));
        log.info("supplement with id " + editsupplementDto.getId() + " was updated");
        return "redirect:" + "/supplementlist";
    }


    @PostMapping("/newsupplement")
    public String newSupplement(@Valid @ModelAttribute("supplementDto")
                                        SupplementDto supplementDto) {
        supplementService.save(supplementDto);
        return "redirect:" + "/supplementlist?created";
    }


    @GetMapping("/newsupplement")
    public String showNewSupplement(ModelMap modelMap) {
        return "newsupplement";
    }
}
