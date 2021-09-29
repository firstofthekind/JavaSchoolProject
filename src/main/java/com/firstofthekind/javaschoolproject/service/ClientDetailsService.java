package com.firstofthekind.javaschoolproject.service;

import com.firstofthekind.javaschoolproject.dto.ClientDto;
import com.firstofthekind.javaschoolproject.entity.ClientEntity;
import com.firstofthekind.javaschoolproject.entity.RoleEntity;
import com.firstofthekind.javaschoolproject.repository.ClientRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Log4j2
@Service
public class ClientDetailsService {

    @Autowired
    ClientRepository clientRepository;
/*
    @Transactional(readOnly = true)
    public UserDetails loadClientByEmail(String email) throws UsernameNotFoundException {
        ClientEntity client = clientRepository.findByEmail(email);
        if (client == null) {
            log.info("Client with email " + email + " is not found");
            throw new UsernameNotFoundException("Client is not found");
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (RoleEntity role :
                client.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getShortName()));
        }
        log.info("Client with email " + email + " is loaded");
        return new ClientEntity(client.getEmail(), client.getPassword());
    }*/
}
