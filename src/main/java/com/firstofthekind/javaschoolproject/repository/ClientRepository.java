package com.firstofthekind.javaschoolproject.repository;

import com.firstofthekind.javaschoolproject.dto.ClientDto;
import com.firstofthekind.javaschoolproject.dto.ContractDto;
import com.firstofthekind.javaschoolproject.entity.ClientEntity;
import com.firstofthekind.javaschoolproject.entity.ContractEntity;
import com.firstofthekind.javaschoolproject.utils.ObjectMapperUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    ClientEntity save(ClientEntity client);
    ClientEntity findByEmail(String email);
    ClientEntity findByContracts(ContractEntity contract);

    ClientEntity findById(long id);

    Boolean existsByEmail(String email);
}
