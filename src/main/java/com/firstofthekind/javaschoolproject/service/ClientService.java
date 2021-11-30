package com.firstofthekind.javaschoolproject.service;

import com.firstofthekind.javaschoolproject.dto.ClientDto;
import com.firstofthekind.javaschoolproject.dto.RegDto;
import com.firstofthekind.javaschoolproject.dto.TariffDto;
import com.firstofthekind.javaschoolproject.entity.ClientEntity;
import com.firstofthekind.javaschoolproject.entity.ContractEntity;
import com.firstofthekind.javaschoolproject.entity.ERole;
import com.firstofthekind.javaschoolproject.entity.RoleEntity;
import com.firstofthekind.javaschoolproject.exception.ClientExistException;
import com.firstofthekind.javaschoolproject.repository.ClientRepository;
import com.firstofthekind.javaschoolproject.repository.RoleRepository;
import com.firstofthekind.javaschoolproject.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public ClientDto getClientDto(long id) {
        ClientEntity client = clientRepository.findById(id);
        return ObjectMapperUtils.map(client, ClientDto.class);
    }

    @Transactional
    public ClientDto getClientDtoByEmail(String email) {
        ClientEntity client = clientRepository.findByEmail(email);
        return ObjectMapperUtils.map(client, ClientDto.class);
    }

    @Transactional
    public ClientEntity getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Transactional
    public ClientEntity getClientEntity(long id) {
        return clientRepository.findById(id);
    }

    /**
     * Before saving a new user to the database,
     * it is necessary to check the email address
     * and passport number for uniqueness.
     *
     * @param regDto - client dto for registration from registration.html.
     */
    @Transactional
    public void registerClient(RegDto regDto) {
        if (clientRepository.existsByEmail(regDto.getEmail())) {
            throw new ClientExistException("Error: Client with that email registered already!");
        }
        if (clientRepository.existsByPassport(regDto.getPassport())) {
            throw new ClientExistException("Error: Client with that passport number registered already!");
        }
        if (!regDto.getPassword().equals(regDto.getPasswordConfirm())) {
            return;
        }

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
    }

    /**
     * To update the client in the database,
     * you need to convert clientDto to clientEntity class.
     *
     * @param clientDto
     */
    @Transactional
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
        log.info("Client with id: " + client.getId() + " was updated.");
    }

    @Transactional
    public Iterable<ClientDto> getAll() {
        return ObjectMapperUtils.mapAll(clientRepository.findAll(), ClientDto.class);
    }

    @Transactional
    public void setStatus(long id, boolean b) {
        ClientDto clientDto = getClientDto(id);
        clientDto.setDeleted(b);
        updateClient(clientDto);
        log.info("client's status with id " + clientDto.getId() + " was updated");
    }
}
