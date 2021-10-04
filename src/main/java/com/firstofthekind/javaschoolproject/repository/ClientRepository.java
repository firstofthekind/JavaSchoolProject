package com.firstofthekind.javaschoolproject.repository;

import com.firstofthekind.javaschoolproject.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    ClientEntity findByEmail(String email);

    Boolean existsByEmail(String email);

}
