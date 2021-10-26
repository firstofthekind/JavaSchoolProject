package com.firstofthekind.javaschoolproject.repository;


import com.firstofthekind.javaschoolproject.dto.SupplementDto;
import com.firstofthekind.javaschoolproject.dto.TariffDto;
import com.firstofthekind.javaschoolproject.entity.RoleEntity;
import com.firstofthekind.javaschoolproject.entity.SupplementEntity;
import com.firstofthekind.javaschoolproject.entity.TariffEntity;
import com.firstofthekind.javaschoolproject.service.TariffService;
import com.firstofthekind.javaschoolproject.utils.ObjectMapperUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public interface TariffRepository extends JpaRepository<TariffEntity, Long> {
    Optional<TariffEntity> findById(Long id);

    @Override
    TariffEntity getById(Long aLong);

     LinkedList<TariffEntity> findAll();
}
