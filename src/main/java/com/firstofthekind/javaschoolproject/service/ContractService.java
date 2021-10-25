package com.firstofthekind.javaschoolproject.service;


import com.firstofthekind.javaschoolproject.dto.ClientDto;
import com.firstofthekind.javaschoolproject.dto.ContractDto;
import com.firstofthekind.javaschoolproject.dto.SupplementDto;
import com.firstofthekind.javaschoolproject.dto.TariffDto;
import com.firstofthekind.javaschoolproject.entity.*;
import com.firstofthekind.javaschoolproject.repository.ContractRepository;
import com.firstofthekind.javaschoolproject.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final ClientService clientService;
    private final TariffService tariffService;
    private final SupplementService supplementService;

    public Iterable<ContractDto> getAll() {
        Set<ContractEntity> contractEntities = contractRepository.findAllByTariffIsNotNull();
        return ObjectMapperUtils.mapAllSet(contractEntities, ContractDto.class);
    }

    public Iterable<ContractDto> getAllByClientId(long id) {
        Set<ContractEntity> contractEntities = contractRepository
                .findAllByClientOrderByNumber(clientService.getClientEntity(id));
        return ObjectMapperUtils.mapAllSet(contractEntities, ContractDto.class);
    }

    public Iterable<ContractDto> getAllByClientEmail(String email) {
        Set<ContractEntity> contractEntities = contractRepository
                .findAllByClientOrderByNumber(clientService.getClientByEmail(email));
        return ObjectMapperUtils.mapAllSet(contractEntities, ContractDto.class);
    }


    public ContractDto getById(long id) {
        return ObjectMapperUtils.map(contractRepository
                .findById(id), ContractDto.class);
    }
    public ContractEntity getEntityById(long id) {
        return contractRepository.getById(id);
    }
    public ContractEntity getByNumber(String number) {
        return contractRepository.findByNumber(number);
    }
    public void blockContract(long id, boolean b) {
        ContractEntity contract = contractRepository.getById(id);
        contract.setBlockedByAdmin(b);
        contractRepository.save(contract);
    }
    public void save(ContractEntity contract) {
        contractRepository.save(contract);
    }

    public void save(ContractDto contractDto, ClientDto clientDto,
                     TariffDto tariffDto, Set<SupplementDto> supplementDtos) {
        Set<SupplementEntity> supplementSet = new HashSet<>();
        for (SupplementDto sup : supplementDtos) {
            supplementSet.add(supplementService.getOne(sup.getId()));
        }
        ClientEntity client = clientService.getClientByEmail(clientDto.getEmail());
        TariffEntity tariffEntity = tariffService.getOne(tariffDto.getId());
        double connection = supplementSet.stream().
                mapToDouble(SupplementEntity::getConnectionCost).sum() + tariffEntity.getConnectionCost();
        double price = supplementSet.stream().
                mapToDouble(SupplementEntity::getPrice).sum() + tariffEntity.getPrice();
        ContractEntity contract = new ContractEntity(contractDto.getNumber(), price,
                connection, client, tariffEntity, supplementSet);
        contractRepository.save(contract);
    }

    public ContractEntity save(ContractDto contractDto) {
        return contractRepository.save(ObjectMapperUtils.map(contractDto, ContractEntity.class));
    }
}
