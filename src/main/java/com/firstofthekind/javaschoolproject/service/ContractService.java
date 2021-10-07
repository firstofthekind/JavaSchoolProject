package com.firstofthekind.javaschoolproject.service;


import com.firstofthekind.javaschoolproject.dto.ClientDto;
import com.firstofthekind.javaschoolproject.dto.ContractDto;
import com.firstofthekind.javaschoolproject.entity.ClientEntity;
import com.firstofthekind.javaschoolproject.entity.ContractEntity;
import com.firstofthekind.javaschoolproject.repository.ContractRepository;
import com.firstofthekind.javaschoolproject.utils.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;

    public Iterable<ContractDto> getAllContracts() {
        List<ContractEntity> contractEntities = contractRepository.findAll();
        return ObjectMapperUtils.mapAll(contractEntities, ContractDto.class);
    }

    public ContractDto getContract(long id) {
        return ObjectMapperUtils.map(contractRepository
                .findById(id), ContractDto.class);
    }

    public ContractEntity save(ContractEntity contract) {
        return contractRepository.save(contract);
    }

    public void save(ContractDto contractDto) {
        contractRepository.save(ObjectMapperUtils.map(contractDto, ContractEntity.class));
    }
}
