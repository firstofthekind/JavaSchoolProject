package com.firstofthekind.javaschoolproject.repository;


import com.firstofthekind.javaschoolproject.entity.TariffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.Optional;

@Repository
public interface TariffRepository extends JpaRepository<TariffEntity, Long> {
    Optional<TariffEntity> findById(Long id);

    @Override
    TariffEntity getById(Long aLong);

    LinkedList<TariffEntity> findAll();
}
