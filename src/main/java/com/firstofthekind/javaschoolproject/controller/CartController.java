package com.firstofthekind.javaschoolproject.controller;

import com.firstofthekind.javaschoolproject.dto.ClientDto;
import com.firstofthekind.javaschoolproject.dto.ContractDto;
import com.firstofthekind.javaschoolproject.dto.SupplementDto;
import com.firstofthekind.javaschoolproject.dto.TariffDto;
import com.firstofthekind.javaschoolproject.service.ClientService;
import com.firstofthekind.javaschoolproject.service.ContractService;
import com.firstofthekind.javaschoolproject.service.SupplementService;
import com.firstofthekind.javaschoolproject.service.TariffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class CartController {

    private final ContractService contractService;
    private final TariffService tariffService;
    private final SupplementService supplementService;
    private final ClientService clientService;

    @GetMapping("/cart")
    public String showCart(Model model, HttpSession session) {
        return "/cart";
    }

    @GetMapping("/tariffs/select/{tariffId}")
    public String addTariff(@PathVariable long tariffId, ModelMap map, HttpSession session) {
        log.info("tariff prowel");
        session.setAttribute("tariff", tariffService.getById(tariffId));
        return "redirect:/supplements";
    }

    @GetMapping("/supplements")
    public String showSupplements(ModelMap modelMap, HttpSession session) {
        if (session.getAttribute("tariff") == null) {
            log.info("u should add tariff first");
            return "redirect:" + "/tariffs";
        } else if (session.getAttribute("selected") == null) {
            List<SupplementDto> supplements = new LinkedList<>();
            session.setAttribute("selected", supplements);
        }
        modelMap.put("supplements", supplementService.getAll());
        return "supplements";
    }

    @GetMapping("/supplements/select/{supplementId}")
    public String addSupplement(@PathVariable long supplementId,
                                ModelMap modelMap, HttpSession session) {
        log.info("supplement prowel");
        if (session.getAttribute("tariff") == null) {
            log.info("u should add tariff first");
        } else if (session.getAttribute("selected") == null) {
            List<SupplementDto> supplements = new LinkedList<>();
            supplements.add(supplementService.getById(supplementId));
            session.setAttribute("selected", supplements);
            modelMap.put("selected", supplements);
        } else {
            List<SupplementDto> supplements = (LinkedList<SupplementDto>) session.getAttribute("selected");
            supplements.add(supplementService.getById(supplementId));
            session.setAttribute("selected", supplements);
            modelMap.put("selected", supplements);
        }
        return "redirect:" + "/supplements";
    }

    @PostMapping("/addnewcontract")
    public String addNewContract(ModelMap map, HttpSession session) {

        contractService.addNewContract(getEmail(session),
                (TariffDto) session.getAttribute("tariff"),
                (LinkedList<SupplementDto>) session.getAttribute("selected"));
        return "redirect:" + "/profile";
    }
/*
    @PostMapping("/updatecontract")
    public String updateContract(ModelMap map, HttpSession session) {

        contractService.updateContract(getEmail(session),
                (TariffDto) session.getAttribute("tariff"),
                (LinkedList<SupplementDto>) session.getAttribute("selected"));
        return "redirect:" + "/profile";
    }*/


    @GetMapping("/supplements/delete/{supplementId}")
    public String delSupplement(@PathVariable long supplementId,
                                ModelMap map, HttpSession session) {
        List<SupplementDto> supplements = (LinkedList<SupplementDto>) session.getAttribute("selected");
        if (supplements != null) {
            supplements.remove(supplementService.getById(supplementId));
            session.setAttribute("selected", supplements);
        } else {
            session.removeAttribute("selected");
        }
        log.info("supplement udalen");
        return "redirect:" + "/supplements";
    }

    private String getEmail(HttpSession session) {
        if (session.getAttribute("client") == null) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user.getUsername();
        } else {
            ClientDto clientDto = (ClientDto) session.getAttribute("client");
            return clientDto.getEmail();
        }
    }
}
