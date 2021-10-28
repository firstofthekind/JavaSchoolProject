package com.firstofthekind.javaschoolproject.service;


import com.firstofthekind.javaschoolproject.dto.SupplementDto;
import com.firstofthekind.javaschoolproject.dto.TariffDto;
import com.firstofthekind.javaschoolproject.entity.SupplementEntity;
import com.firstofthekind.javaschoolproject.entity.TariffEntity;
import com.firstofthekind.javaschoolproject.repository.TariffRepository;
import com.firstofthekind.javaschoolproject.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class TariffService {
    private final TariffRepository tariffRepository;

    @Transactional
    public Iterable<TariffDto> getAll() {
        return ObjectMapperUtils.mapAll(tariffRepository.findAll(), TariffDto.class);
    }
    @Transactional
    public TariffDto getById(long id){
        return ObjectMapperUtils.map(tariffRepository.getById(id), TariffDto.class);
    }

    @Transactional
    public TariffEntity getOne(long id) {
        return tariffRepository.getById(id);
    }

    @Transactional
    public void save(TariffDto tariffDto) {
        tariffRepository.save(ObjectMapperUtils.map(tariffDto, TariffEntity.class));
        log.info("Tariff "+tariffDto.getTitle()+" created.");
    }

    @Transactional
    public void updateTariffDto(TariffDto tariffDto) {
        tariffRepository.save(ObjectMapperUtils.map(tariffDto, TariffEntity.class));
        log.info("Tariff "+tariffDto.getTitle()+" updated.");
    }
    @Transactional
    public void setDeleted(long id, boolean b){
        TariffDto tariffDto = getById(id);
        tariffDto.setDeleted(b);
        save(tariffDto);
        log.info("tariff's status with id " + tariffDto.getId() + " was updated");
    }
}
