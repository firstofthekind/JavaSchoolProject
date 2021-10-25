package com.firstofthekind.javaschoolproject.service;


import com.firstofthekind.javaschoolproject.dto.SupplementDto;
import com.firstofthekind.javaschoolproject.dto.TariffDto;
import com.firstofthekind.javaschoolproject.entity.SupplementEntity;
import com.firstofthekind.javaschoolproject.entity.TariffEntity;
import com.firstofthekind.javaschoolproject.repository.SupplementRepository;
import com.firstofthekind.javaschoolproject.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j2
public class SupplementService {
    private final SupplementRepository supplementRepository;

    public Iterable<SupplementDto> getAll() {
        return ObjectMapperUtils.mapAll(supplementRepository.findAll(), SupplementDto.class);
    }

    public SupplementEntity getOne(long id) {
        return supplementRepository.getById(id);
    }

    public SupplementDto getById(long id) {
        return ObjectMapperUtils.map(supplementRepository.getById(id), SupplementDto.class);
    }

    public void save(SupplementDto supplementDto) {
        supplementRepository.save(ObjectMapperUtils.map(supplementDto, SupplementEntity.class));
        log.info("Supplement " + supplementDto.getTitle() + " created.");
    }

    void updateSupplementDto(SupplementDto supplementDto) {
        supplementRepository.save(ObjectMapperUtils.map(supplementDto, SupplementEntity.class));
    }
}
