package com.firstofthekind.javaschoolproject.repository;


import com.firstofthekind.javaschoolproject.entity.RoleEntity;
import com.firstofthekind.javaschoolproject.entity.TariffEntity;
import com.firstofthekind.javaschoolproject.service.TariffService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TariffRepository extends JpaRepository<TariffEntity, Long> {

    List<TariffEntity> findAll();
}
