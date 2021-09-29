package com.firstofthekind.javaschoolproject.service;

import com.firstofthekind.javaschoolproject.dto.RegDto;
import com.firstofthekind.javaschoolproject.entity.ClientEntity;
import com.firstofthekind.javaschoolproject.entity.ERole;
import com.firstofthekind.javaschoolproject.entity.RoleEntity;
import com.firstofthekind.javaschoolproject.repository.ClientRepository;
import com.firstofthekind.javaschoolproject.repository.RoleRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;
import java.net.http.HttpClient;
import java.util.HashSet;
import java.util.Set;

@Log4j2
@Service
public class ClientService {
    @Autowired
    private RoleService roleService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    RoleRepository roleRepository;

    /*
        @Autowired
        UserDetailsService userDetailsService;

        @Autowired
        AuthenticationManager authenticationManager;*/
/*
    @Transactional
    public void login(String email, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails,
                password,
                userDetails.getAuthorities());
        authenticationManager.authenticate(token);
        if (token.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(token);
            log.info("Auto login " + email + " is successful");
        } else {
            log.info("Auto login " + email + " is not successful");
        }
    }
*/
    public ResponseEntity<?> registerClient(RegDto regDto) {


        if (clientRepository.existsByEmail(regDto.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new String("Error: Email is already in use!"));
        }
        if (!regDto.getPassword().equals(regDto.getPasswordConfirm())) {
            return ResponseEntity
                    .badRequest()
                    .body(new String("Error: Passwords don' match!"));
        }

        // Create new client's account - to controller
        ClientEntity client = new ClientEntity(
                regDto.getFirstname(),
                regDto.getLastname(),
                regDto.getBirthdate(),
                regDto.getPassport(),
                regDto.getAddress(),
                regDto.getEmail(),
                passwordEncoder.encode(regDto.getPassword()));

        Set<String> strRoles = regDto.getRole();
        Set<RoleEntity> roles = new HashSet<>();

        if (!strRoles.contains("admin")) {
                RoleEntity clientRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(clientRole);
        } else {
                    RoleEntity adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                }

        client.setRoles(roles);
        clientRepository.save(client);
        String roleList = new RoleEntity().getShortNames(roles);
        log.info("New client was created." +
                "\n Email: "+ client.getEmail()+ "; Role " + roleList);
        return null;
    }
}
