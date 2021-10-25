package com.firstofthekind.javaschoolproject.controller;

import com.firstofthekind.javaschoolproject.dto.*;
import com.firstofthekind.javaschoolproject.entity.ContractEntity;
import com.firstofthekind.javaschoolproject.exception.CodependentSupplementException;
import com.firstofthekind.javaschoolproject.exception.IncompatibleSupplementException;
import com.firstofthekind.javaschoolproject.service.*;
import com.firstofthekind.javaschoolproject.utils.ObjectMapperUtils;
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
import java.util.*;

@Controller
@RequiredArgsConstructor
@Log4j2
public class CartController {

    private final CartService cartService;
    private final ContractService contractService;
    private final TariffService tariffService;
    private final SupplementService supplementService;
    private final ClientService clientService;

    @GetMapping("/cart")
    public String showCart(Model model, HttpSession session) {
        return "/cart";
    }

    /*
        @PostMapping("/cart")
        public String update(HttpSession session) {
            CartDto cartDto = (CartDto) session.getAttribute("cart");
            cartService.updateContract(cartDto);
            return "redirect:/";
        }*/
/*
    @PostMapping("/tariffs/select/{tariffId}")
    public String addTariff(@PathVariable long tariffId, HttpSession session) {
        log.info("tariff prowel");
        CartDto cart = getCart(session);
        cartService.addTariff(cart, tariffId);
        return "redirect:/contract/connectOptions/{contractId}";
    }*/

    @GetMapping("/tariffs/select/{tariffId}")
    public String addTariff(@PathVariable long tariffId, ModelMap map, HttpSession session) {
        log.info("tariff prowel");
        session.setAttribute("tariff", tariffService.getById(tariffId));
        return "redirect:/supplements";
    }

    @GetMapping("/supplements")
    public String showSupplements(ModelMap modelMap, HttpSession session) {
        if (session.getAttribute("selected") == null) {
            Set<SupplementDto> supplements = new HashSet<>();
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
            Set<SupplementDto> supplements = new HashSet<>();
            supplements.add(supplementService.getById(supplementId));
            session.setAttribute("selected", supplements);
            modelMap.put("selected", supplements);
        } else {
            Set<SupplementDto> supplements = (HashSet<SupplementDto>) session.getAttribute("selected");
            supplements.add(supplementService.getById(supplementId));
            session.setAttribute("selected", supplements);
            modelMap.put("selected", supplements);
        }
        return "redirect:" + "/supplements";
    }

    @PostMapping("/addnewcontract")
    public String addNewContract(ModelMap map, HttpSession session) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        ClientDto clientDto1 = clientService.getClientDtoByEmail(email);
        ContractDto contractDto = new ContractDto();
        long num = (1000000 + (long) (Math.random() * ((999999 - 100000) + 1)));
        contractDto.setNumber(Long.toString(num));
        contractService.save(contractDto, clientService.getClientDtoByEmail(email),
                (TariffDto) session.getAttribute("tariff")
                , (Set<SupplementDto>) session.getAttribute("selected"));
        return "redirect:" + "/profile";
    }

    @GetMapping("/supplements/delete/{supplementId}")
    public String delSupplement(@PathVariable long supplementId,
                                ModelMap map, HttpSession session) {
        Set<SupplementDto> supplements = (HashSet<SupplementDto>) session.getAttribute("selected");
        if (supplements != null) {
            supplements.remove(supplementService.getById(supplementId));
            session.setAttribute("selected", supplements);
        } else {
            session.removeAttribute("selected");
        }
        log.info("supplement udalen");
        return "redirect:" + "/supplements";
    }

/*
    @GetMapping(value = "/supplements")
    public String showSupplements(ModelMap model,
                                  HttpSession session) {
        showCartSupplements(model, session);
        return "/supplements";
    }

    @PostMapping("/supplements/add/{supplementID}")
    public String addSupplement(@PathVariable Long supplementID,
                                HttpSession session, ModelMap model) {
        CartDto cart = getCart(session);
        try {
            cartService.addSupplement(cart, supplementID);
            model.addAttribute("added", "Supplement added to the cart");
        } catch (IncompatibleSupplementException | CodependentSupplementException e) {
            model.addAttribute("failure", e.getMessage());
        }
        showSupplements(model, session);
        return "/supplements";
    }*/
/*
    @PostMapping("/supplements/del/{supplementID}")
    public String deleteSupplement(@PathVariable Long supplementID,
                                   HttpSession session) {
        CartDto cart = (CartDto) session.getAttribute("cart");
        cartService.deleteSupplement(cart, supplementID);
        return "redirect:/cart";
    }


    @PostMapping("/cart/deleteItem/")
    public String deleteItem(HttpSession session) {
        CartDto cartDto = (CartDto) session.getAttribute("cart");
        cartService.deleteItemDto(cartDto);
        return "redirect:/cart";
    }


    private CartDto getCart(HttpSession session) {
        CartDto cartDto = (CartDto) session.getAttribute("cart");
        if (cartDto == null) {
            cartDto = new CartDto();
            session.setAttribute("cart", cartDto);
        }
        return cartDto;
    }

    private void showCartSupplements(ModelMap model, HttpSession session) {
        CartDto cartDto = getCart(session);
        model.addAttribute("availableSupplements",
                cartService.getAvailableSupplements(cartDto));
    }*/
}
