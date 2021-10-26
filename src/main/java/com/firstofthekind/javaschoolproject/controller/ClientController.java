package com.firstofthekind.javaschoolproject.controller;

import com.firstofthekind.javaschoolproject.dto.ClientDto;
import com.firstofthekind.javaschoolproject.dto.ContractDto;
import com.firstofthekind.javaschoolproject.service.ClientService;
import com.firstofthekind.javaschoolproject.service.ContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.LinkedList;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ClientController {
    private final ClientService clientService;
    private final ContractService contractService;

    @GetMapping("/clients")
    public String userList() {
        return "clientlist";
    }

    @GetMapping("/profile")
    public String showProfile(ModelMap modelMap) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        modelMap.put("currentClient", clientService.getClientDtoByEmail(email));
        modelMap.put("contracts", (LinkedList<ContractDto>) contractService.getAllByClientEmail(email));
        return "profile";
    }

    @GetMapping("/profileedit")
    public String showEditProfile(ModelMap modelMap) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        modelMap.put("currentClient", clientService.getClientDtoByEmail(user.getUsername()));
        return "/profileedit";
    }

    @PostMapping("/profileedit")
    public String editProfile(ModelMap modelMap,
                              @ModelAttribute("editClientDto") ClientDto clientDto) {
        clientService.updateClient(clientDto);
        return "/main";
    }

    @PostMapping("/client?{clientID}")
    public void updateClient(ClientDto clientDto) {
        clientService.updateClient(clientDto);
    }

    @GetMapping("/client?{clientID}")
    public String getClient(@PathVariable Long clientID, HttpSession session) {
        ClientDto clientDto = new ClientDto();
        try {
            clientDto = clientService.getClientDto(clientID);
        } catch (Exception e) {
            log.info("Client with ID " + clientID + " not found");
            return "redirect:/client?notfound";
        }
        if (session.getAttribute("client") == null) {
            session.setAttribute("client", clientDto);
        } else if (!session.getAttribute("client").equals(clientDto)) {
            session.setAttribute("client", clientDto);
        }
        return "/client?" + clientID;
    }

}
