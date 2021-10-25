package com.firstofthekind.javaschoolproject.service;

import com.firstofthekind.javaschoolproject.entity.ClientEntity;
import com.firstofthekind.javaschoolproject.entity.RoleEntity;
import com.firstofthekind.javaschoolproject.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClientDetailsService implements UserDetailsService {

@Autowired
    private ClientRepository clientRepository;


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ClientEntity client = clientRepository.findByEmail(username);
        if (client == null) {
            log.info("Client not found");
            throw new UsernameNotFoundException("Invalid email or password in back");
        }
        log.info("Client" + client.getEmail() + " logged in");
        return new org.springframework.security.core.userdetails.User(client.getEmail(), client.getPassword(), mapRolesToAuthorities(client.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<RoleEntity> roleEntities) {
        return roleEntities.stream().map(roleEntity ->
                new SimpleGrantedAuthority(roleEntity.getShortName())).collect(Collectors.toList());
    }

}
