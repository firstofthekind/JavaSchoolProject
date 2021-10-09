package com.firstofthekind.javaschoolproject.service;


import com.firstofthekind.javaschoolproject.dto.SupplementDto;
import com.firstofthekind.javaschoolproject.dto.TariffDto;
import com.firstofthekind.javaschoolproject.entity.SupplementEntity;
import com.firstofthekind.javaschoolproject.entity.TariffEntity;
import com.firstofthekind.javaschoolproject.repository.SupplementRepository;
import com.firstofthekind.javaschoolproject.repository.TariffRepository;
import com.firstofthekind.javaschoolproject.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SupplementService {
    @Autowired
    private SupplementRepository supplementRepository;

    public Iterable<TariffDto> getAll() {
        return ObjectMapperUtils.mapAll(supplementRepository.findAll(), TariffDto.class);
    }

    public SupplementDto getOne(long id) {
        return ObjectMapperUtils.map(supplementRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found")),
                SupplementDto.class);
    }

    public SupplementEntity save(TariffDto tariffDto) {
        return supplementRepository.save(ObjectMapperUtils.map(tariffDto, SupplementEntity.class));
    }
}
