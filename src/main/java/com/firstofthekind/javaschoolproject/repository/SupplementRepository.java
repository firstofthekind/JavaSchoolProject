package com.firstofthekind.javaschoolproject.repository;

import com.firstofthekind.javaschoolproject.dto.ContractDto;
import com.firstofthekind.javaschoolproject.dto.SupplementDto;
import com.firstofthekind.javaschoolproject.entity.ContractEntity;
import com.firstofthekind.javaschoolproject.entity.SupplementEntity;
import com.firstofthekind.javaschoolproject.entity.TariffEntity;
import com.firstofthekind.javaschoolproject.utils.ObjectMapperUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplementRepository extends JpaRepository<SupplementEntity, Long> {
    List<SupplementEntity> findAll();

    @Override
    SupplementEntity getById(Long id);

    default SupplementDto getSupplementDtoById(Long id) {
        return ObjectMapperUtils.map(getById(id), SupplementDto.class);
    }
}
