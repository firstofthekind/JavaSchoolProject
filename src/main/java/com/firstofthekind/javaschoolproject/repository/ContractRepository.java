package com.firstofthekind.javaschoolproject.repository;


import com.firstofthekind.javaschoolproject.entity.ClientEntity;
import com.firstofthekind.javaschoolproject.entity.ContractEntity;
import com.firstofthekind.javaschoolproject.entity.TariffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedList;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Long> {
    LinkedList<ContractEntity> findAllByClientOrderById(ClientEntity client);

    LinkedList<ContractEntity> findAllByTariffIsNotNullOrderById();

    ContractEntity findByNumber(String number);

    ContractEntity getById(long id);

    LinkedList<ContractEntity> findAllByTariff(TariffEntity tariff);
    ArrayList<ContractEntity> getAllByClientId(long id);
}
