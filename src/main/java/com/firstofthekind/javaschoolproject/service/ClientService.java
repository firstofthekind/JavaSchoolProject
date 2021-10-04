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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Log4j2
@Service
public class ClientService{
    @Autowired
    private RoleService roleService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
        if (!regDto.isCheckbox()){
            return ResponseEntity
                    .badRequest()
                    .body(new String("Error: you must be agree with license terms!"));
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
/*
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ClientEntity client = clientRepository.findByEmail(username);
        if (client==null){
            log.info("Client not found");
            throw new UsernameNotFoundException("Invalid email or password in back");
        }
        log.info("Client" + client.getEmail() +" logged in");
        return new org.springframework.security.core.userdetails.User(client.getEmail(), client.getPassword(), mapRolesToAuthorities(client.getRoles()));
    }

    private Collection<? extends  GrantedAuthority> mapRolesToAuthorities(Collection<RoleEntity> roleEntities){
       return roleEntities.stream().map(roleEntity ->
               new SimpleGrantedAuthority(roleEntity.getShortName())).collect(Collectors.toList());
    }*/

}
