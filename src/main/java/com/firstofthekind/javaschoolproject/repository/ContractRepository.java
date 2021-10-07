package com.firstofthekind.javaschoolproject.repository;


import com.firstofthekind.javaschoolproject.entity.ClientEntity;
import com.firstofthekind.javaschoolproject.entity.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Set;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Long> {

    Set<ContractEntity> findAllByClientOrderByNumber(ClientEntity client);
}
