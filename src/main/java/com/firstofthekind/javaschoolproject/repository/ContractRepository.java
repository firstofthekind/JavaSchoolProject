package com.firstofthekind.javaschoolproject.repository;


import com.firstofthekind.javaschoolproject.dto.ClientDto;
import com.firstofthekind.javaschoolproject.dto.ContractDto;
import com.firstofthekind.javaschoolproject.entity.ClientEntity;
import com.firstofthekind.javaschoolproject.entity.ContractEntity;
import com.firstofthekind.javaschoolproject.entity.TariffEntity;
import com.firstofthekind.javaschoolproject.utils.ObjectMapperUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Set;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Long> {
    Set<ContractEntity> findAllByClientOrderByNumber(ClientEntity client);
    Set<ContractEntity> findAllByTariffIsNotNull();
    ContractEntity findByNumber(String number);
    ContractEntity getById(long id);
}
