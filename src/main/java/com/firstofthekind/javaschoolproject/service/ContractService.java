package com.firstofthekind.javaschoolproject.service;


import com.firstofthekind.javaschoolproject.dto.*;
import com.firstofthekind.javaschoolproject.entity.*;
import com.firstofthekind.javaschoolproject.repository.ContractRepository;
import com.firstofthekind.javaschoolproject.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final ClientService clientService;
    private final TariffService tariffService;
    private final SupplementService supplementService;
    public final MessageSender messageSender;

    @Transactional
    public Iterable<ContractDto> getAll() {
        LinkedList<ContractEntity> contractEntities = contractRepository.findAllByTariffIsNotNullOrderById();
        return ObjectMapperUtils.mapAll(contractEntities, ContractDto.class);
    }

    @Transactional
    public Iterable<ContractDto> getAllByClientId(long id) {
        LinkedList<ContractEntity> contractEntities = contractRepository
                .findAllByClientOrderById(clientService.getClientEntity(id));
        return ObjectMapperUtils.mapAll(contractEntities, ContractDto.class);
    }

    @Transactional
    public Iterable<ContractDto> getAllByClientEmail(String email) {
        LinkedList<ContractEntity> contractEntities = contractRepository
                .findAllByClientOrderById(clientService.getClientByEmail(email));
        return ObjectMapperUtils.mapAll(contractEntities, ContractDto.class);
    }


    @Transactional
    public ContractDto getById(long id) {
        return ObjectMapperUtils.map(contractRepository
                .getById(id), ContractDto.class);
    }

    @Transactional
    public ContractEntity getEntityById(long id) {
        return contractRepository.getById(id);
    }

    @Transactional
    public ContractEntity getByNumber(String number) {
        return contractRepository.findByNumber(number);
    }

    @Transactional
    public void blockContractByAdmin(long id, boolean b) {
        ContractEntity contract = contractRepository.getById(id);
        contract.setBlockedByAdmin(b);
        contractRepository.save(contract);
    }

    @Transactional
    public void blockContractByClient(long id, boolean b) {
        ContractEntity contract = contractRepository.getById(id);
        contract.setBlockedByClient(b);
        contractRepository.save(contract);
    }

    @Transactional
    public void editContractNum(ContractDto contractDto) {
        ContractEntity contract = contractRepository.getById(contractDto.getId());
        contract.setNumber(contractDto.getNumber());
        contractRepository.save(contract);
    }

    @Transactional
    public void save(ContractEntity contract) {
        contractRepository.save(contract);

        List<TariffJsonDto> tariffDtos = tariffService.getTariffsWithCount();
        messageSender.sendMessage(tariffDtos);
    }

    @Transactional
    public void save(ContractDto contractDto, TariffDto tariffDto,
                     LinkedList<SupplementSelectDto> supplementDtos) {
        ContractEntity entity = contractRepository.getById(contractDto.getId());
        LinkedList<SupplementEntity> supplementSet = new LinkedList<>();
        for (SupplementSelectDto sup : supplementDtos) {
            supplementSet.add(supplementService.getOne(sup.getId()));
        }
        entity.setTariff(tariffService.getOne(tariffDto.getId()));
        TariffEntity tariffEntity = tariffService.getOne(tariffDto.getId());
        double connection = supplementSet.stream().
                mapToDouble(SupplementEntity::getConnectionCost).sum() + tariffEntity.getConnectionCost();
        double price = supplementSet.stream().
                mapToDouble(SupplementEntity::getPrice).sum() + tariffEntity.getPrice();
        entity.setConnectionCost(connection);
        entity.setPrice(price);
        entity.setSupplement(supplementSet);
        contractRepository.save(entity);

        List<TariffJsonDto> tariffDtos = tariffService.getTariffsWithCount();
        messageSender.sendMessage(tariffDtos);
    }

    @Transactional
    public void save(ContractDto contractDto, ClientDto clientDto,
                     TariffDto tariffDto, LinkedList<SupplementSelectDto> supplementDtos) {
        LinkedList<SupplementEntity> supplementSet = new LinkedList<>();
        for (SupplementSelectDto sup : supplementDtos) {
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

        List<TariffJsonDto> tariffDtos = tariffService.getTariffsWithCount();
        messageSender.sendMessage(tariffDtos);
    }

    @Transactional
    public void addNewContract(String email,
                               TariffDto tariffDto,
                               LinkedList<SupplementSelectDto> supplementDtos
    ) {
        ContractDto contractDto = new ContractDto();
        long num = (79030000000L + (long) (Math.random() * ((79039999999L - 79030000000L) + 1)));
        contractDto.setNumber(Long.toString(num));
        save(contractDto, clientService.getClientDtoByEmail(email), tariffDto, supplementDtos);

    }

}
