package com.firstofthekind.javaschoolproject.service;


import com.firstofthekind.javaschoolproject.dto.TariffDto;
import com.firstofthekind.javaschoolproject.entity.ContractEntity;
import com.firstofthekind.javaschoolproject.entity.TariffEntity;
import com.firstofthekind.javaschoolproject.repository.TariffRepository;
import com.firstofthekind.javaschoolproject.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TariffService {
    @Autowired
    private TariffRepository tariffRepository;

    public Iterable<TariffDto> getAllTariffs() {
        return ObjectMapperUtils.mapAll(tariffRepository.findAll(), TariffDto.class);
    }

    public TariffDto getProduct(long id) {
        return ObjectMapperUtils.map(tariffRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found")),
                TariffDto.class);
    }

    public TariffEntity save(TariffDto tariffDto) {
        return tariffRepository.save(ObjectMapperUtils.map(tariffDto, TariffEntity.class));
    }
}
