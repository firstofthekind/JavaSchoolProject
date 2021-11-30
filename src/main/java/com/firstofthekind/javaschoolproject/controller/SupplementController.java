package com.firstofthekind.javaschoolproject.controller;


import com.firstofthekind.javaschoolproject.dto.SupplementDto;
import com.firstofthekind.javaschoolproject.dto.SupplementSelectDto;
import com.firstofthekind.javaschoolproject.dto.TariffDto;
import com.firstofthekind.javaschoolproject.dto.TariffJsonDto;
import com.firstofthekind.javaschoolproject.exception.CodependentSupplementException;
import com.firstofthekind.javaschoolproject.exception.IncompatibleSupplementException;
import com.firstofthekind.javaschoolproject.service.ContractService;
import com.firstofthekind.javaschoolproject.service.MessageSender;
import com.firstofthekind.javaschoolproject.service.SupplementService;
import com.firstofthekind.javaschoolproject.service.TariffService;
import com.firstofthekind.javaschoolproject.utils.Merge;
import com.firstofthekind.javaschoolproject.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class SupplementController {
    private final SupplementService supplementService;
    private final ContractService contractService;
    private final TariffService tariffService;
    public final MessageSender messageSender;


    @GetMapping("/supplementlist")
    public String showAllSupplements(ModelMap modelMap) {
        modelMap.put("supplements", supplementService.getAll());
        return "supplementlist";
    }

    @GetMapping("/supplements/{supplementId}")
    public String showAvailable(ModelMap modelMap, HttpSession session) {
        TariffDto tariffDto = ObjectMapperUtils.map(session.getAttribute("tariff"), TariffDto.class);
        modelMap.put("supplements", supplementService.getAllSelect(tariffDto.getId()));
        return "supplements";
    }

    @GetMapping("/supplements/c/{contractId}")
    public String showContractSupplements(@PathVariable("contractId") long id,
                                          ModelMap modelMap, HttpSession session) {
        session.removeAttribute("selected");
        modelMap.remove("selected");
        session.setAttribute("tariff", tariffService.getById(contractService.getById(id).getTariff().getId()));
        session.setAttribute("contract", contractService.getById(id));
        return "redirect:/supplements";
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

        List<TariffJsonDto> tariffDtos = tariffService.getTariffsWithCount();
        messageSender.sendMessage(tariffDtos);
        log.info("supplement with id " + editsupplementDto.getId() + " was updated");
        return "redirect:" + "/supplementlist";
    }

    @GetMapping("/editrelatedsup/{supid}")
    public String showEditRelatedSupplement(@PathVariable("supid") long id, ModelMap modelMap) {
        SupplementDto supDto = supplementService.getById(id);
        modelMap.put("supplement", supDto);
        modelMap.put("compatible", supplementService.getCompatible(id));
        modelMap.put("independent", supplementService.getIndependentSupplements(id));
        modelMap.put("incompatible", supplementService.getIncompatible(id));
        modelMap.put("codependent", supplementService.getDependentSupplements(id));
        modelMap.put("available", supplementService.getAvailableSupplements(id));
        return "editrelatedsup";
    }

    @PostMapping("/editrelatedsup/{supid}/dep/{relsupid}")
    public String addDependent(@PathVariable("supid") long id,
                               @PathVariable("relsupid") long relId,
                               @ModelAttribute("editSupplementDto") SupplementDto editsupplementDto,
                               ModelMap modelMap) throws InvocationTargetException, IllegalAccessException, InstantiationException, IncompatibleSupplementException {
        supplementService.addDependent(id, relId);
        log.info("supplement with id " + editsupplementDto.getId() + " was updated");
        return "redirect:" + "/editrelatedsup/" + id;
    }

    @GetMapping("/editrelatedsup/{supid}/dep/{relsupid}")
    public String delDependent(@PathVariable("supid") long id,
                               @PathVariable("relsupid") long relId,
                               @ModelAttribute("editSupplementDto") SupplementDto editsupplementDto,
                               ModelMap modelMap) throws InvocationTargetException, IllegalAccessException, InstantiationException, IncompatibleSupplementException {
        supplementService.deleteDependent(id, relId);
        log.info("supplement with id " + editsupplementDto.getId() + " was updated");
        return "redirect:" + "/editrelatedsup/" + id;
    }

    @PostMapping("/editrelatedsup/{supid}/inc/{relsupid}")
    public String addIncompatible(@PathVariable("supid") long id,
                                  @PathVariable("relsupid") long relId,
                                  @ModelAttribute("editSupplementDto") SupplementDto editsupplementDto,
                                  ModelMap modelMap) throws InvocationTargetException, IllegalAccessException, InstantiationException, IncompatibleSupplementException, CodependentSupplementException {
        supplementService.addIncompatible(id, relId);
        log.info("supplement with id " + editsupplementDto.getId() + " was updated");
        return "redirect:" + "/editrelatedsup/" + id;
    }

    @GetMapping("/editrelatedsup/{supid}/inc/{relsupid}")
    public String delIncompatible(@PathVariable("supid") long id,
                                  @PathVariable("relsupid") long relId,
                                  @ModelAttribute("editSupplementDto") SupplementDto editsupplementDto,
                                  ModelMap modelMap) throws InvocationTargetException, IllegalAccessException, InstantiationException, IncompatibleSupplementException {
        supplementService.deleteIncompatible(id, relId);
        log.info("supplement with id " + editsupplementDto.getId() + " was updated");
        return "redirect:" + "/editrelatedsup/" + id;
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
