package com.firstofthekind.javaschoolproject.service;

import com.firstofthekind.javaschoolproject.dto.ClientDto;
import com.firstofthekind.javaschoolproject.dto.RegDto;
import com.firstofthekind.javaschoolproject.dto.TariffDto;
import com.firstofthekind.javaschoolproject.entity.ClientEntity;
import com.firstofthekind.javaschoolproject.entity.ContractEntity;
import com.firstofthekind.javaschoolproject.entity.ERole;
import com.firstofthekind.javaschoolproject.entity.RoleEntity;
import com.firstofthekind.javaschoolproject.repository.ClientRepository;
import com.firstofthekind.javaschoolproject.repository.RoleRepository;
import com.firstofthekind.javaschoolproject.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClientService {

    private final RoleService roleService;
    private final ClientRepository clientRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ClientDto getClientDto(long id) {
        ClientEntity client = clientRepository.findById(id);
        return ObjectMapperUtils.map(client, ClientDto.class);
    }

    public ClientDto getClientDtoByEmail(String email) {
        ClientEntity client = clientRepository.findByEmail(email);
        return ObjectMapperUtils.map(client, ClientDto.class);
    }

    public ClientEntity getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    public ClientEntity getClientEntity(long id) {
        return clientRepository.findById(id);
    }

    public String registerClient(RegDto regDto) {
        if (clientRepository.existsByEmail(regDto.getEmail())) {
            return "Error: Email is already in use!";
        }
        if (!regDto.getPassword().equals(regDto.getPasswordConfirm())) {
            return "Error: Passwords don' match!";
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

        List<String> strRoles = regDto.getRole();
        List<RoleEntity> roles = new LinkedList<>();

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
                "\n Email: " + client.getEmail() + "; Role " + roleList);
        return "login";
    }

    public void updateClient(ClientDto clientDto) {
        ClientEntity client = ObjectMapperUtils.map(clientDto, ClientEntity.class);
        if (client.getRoles() == null & clientDto.getRolesStr() == null || clientDto.getRolesStr() == null) {
            client.setRoles(clientRepository.findById(clientDto.getId()).getRoles());
        } else {
            List<RoleEntity> roleEntities = new LinkedList<>();
            for (String role : clientDto.getRolesStr()) {
                roleEntities.add(roleRepository.findAllByName(ERole.valueOf(role)));
            }
            client.setRoles(roleEntities);
        }
        client.setPassword(clientRepository.findById(clientDto.getId()).getPassword());
        clientRepository.save(client);
    }

    public void updateClient(RegDto clientDto) {
        ClientEntity client = clientRepository.findByEmail(clientDto.getEmail());
        client = ObjectMapperUtils.map(clientDto, ClientEntity.class);
        clientRepository.save(client);
    }

    public Iterable<ClientDto> getAll() {
        return ObjectMapperUtils.mapAll(clientRepository.findAll(), ClientDto.class);
    }

    public void setStatus(long id, boolean b){
        ClientDto clientDto = getClientDto(id);
        clientDto.setDeleted(b);
        updateClient(clientDto);
        log.info("client's status with id " + clientDto.getId() + " was updated");
    }
}
