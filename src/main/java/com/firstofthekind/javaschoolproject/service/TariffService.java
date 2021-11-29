package com.firstofthekind.javaschoolproject.service;


import com.firstofthekind.javaschoolproject.dto.SupplementDto;
import com.firstofthekind.javaschoolproject.dto.SupplementSelectDto;
import com.firstofthekind.javaschoolproject.dto.TariffDto;
import com.firstofthekind.javaschoolproject.dto.TariffJsonDto;
import com.firstofthekind.javaschoolproject.entity.SupplementEntity;
import com.firstofthekind.javaschoolproject.entity.TariffEntity;
import com.firstofthekind.javaschoolproject.exception.IncompatibleSupplementException;
import com.firstofthekind.javaschoolproject.repository.ContractRepository;
import com.firstofthekind.javaschoolproject.repository.SupplementRepository;
import com.firstofthekind.javaschoolproject.repository.TariffRepository;
import com.firstofthekind.javaschoolproject.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class TariffService {
    private final TariffRepository tariffRepository;
    private final ContractRepository contractRepository;
    private final SupplementRepository supplementRepository;
    private final SupplementService supplementService;
    public final MessageSender messageSender;

    @Transactional
    public Iterable<TariffDto> getAll() {
        Iterable<TariffDto> tariffDtos = ObjectMapperUtils.mapAll(tariffRepository.findAll(), TariffDto.class);
        for (TariffDto tariffDto : tariffDtos) {
            tariffDto.setSupplementDtoSet(
                    ObjectMapperUtils.mapAll(supplementService.getTariffSupplements(tariffDto.getId()), SupplementDto.class));
        }
        return tariffDtos;
    }
    @Transactional
    public Iterable<TariffJsonDto> getAllWithoutSupplements() {
        Iterable<TariffJsonDto> tariffDtos = ObjectMapperUtils.mapAll(tariffRepository.findAll(), TariffJsonDto.class);
        return tariffDtos;
    }

    @Transactional
    public TariffDto getById(long id) {
        TariffDto tariffDto = ObjectMapperUtils.map(tariffRepository.getById(id), TariffDto.class);
        tariffDto.setSupplementDtoSet(
                ObjectMapperUtils.mapAll(supplementService.getTariffSupplements(tariffDto.getId()), SupplementDto.class));
        return tariffDto;
    }

    @Transactional
    public TariffEntity getOne(long id) {
        return tariffRepository.getById(id);
    }

    @Transactional
    public void save(TariffDto tariffDto) {
        TariffEntity tariff = ObjectMapperUtils.map(tariffDto, TariffEntity.class);
        tariff.setSupplement(ObjectMapperUtils.mapAll(tariffDto.getSupplementDtoSet(), SupplementEntity.class));
        tariffRepository.save(tariff);
        log.info("Tariff " + tariffDto.getTitle() + " created.");

        List<TariffJsonDto> tariffDtos = getTariffsWithCount();
        messageSender.sendMessage(tariffDtos);
    }

    @Transactional
    public LinkedList<TariffJsonDto> getTariffsWithCount() {
        LinkedList<TariffJsonDto> tariffDtos = (LinkedList<TariffJsonDto>) getAllWithoutSupplements();
        for (TariffJsonDto dto : tariffDtos) {
            dto.setCount(contractRepository.findAllByTariff(tariffRepository.getById(dto.getId())).size());
            dto.setSupplementDtoSet(ObjectMapperUtils.mapAll(tariffRepository.getById(dto.getId()).getSupplement(), SupplementSelectDto.class));
        }
        tariffDtos.sort(Comparator.comparing(TariffJsonDto::getCount));
        Collections.reverse(tariffDtos);
        return tariffDtos;
    }

    @Transactional
    public void setDeleted(long id, boolean b) {
        TariffDto tariffDto = getById(id);
        tariffDto.setDeleted(b);

        save(tariffDto);
        log.info("tariff's status with id " + tariffDto.getId() + " was updated");
    }

    //Edit supplements
    @Transactional
    public void addSupplement(long tariffId,
                              long supplementId) {
        TariffEntity tariff = tariffRepository.getById(tariffId);
        SupplementEntity second = supplementRepository.getById(supplementId);
        tariff.addOption(second);
        for (SupplementEntity supplement : supplementService.getDependentSupplements(supplementId)) {
            tariff.addOption(supplement);
        }
        try {
            tariffRepository.save(tariff);
            log.info("Tariff " + tariff.getTitle() + " updated.");
        } catch (Exception e) {
            log.info("Error" + e.getMessage());
        }


        List<TariffJsonDto> tariffDtos = getTariffsWithCount();
        messageSender.sendMessage(tariffDtos);
    }

    @Transactional
    public void delSupplement(long tariffId,
                              long supplementId) {
        TariffEntity tariff = tariffRepository.getById(tariffId);
        SupplementEntity second = supplementRepository.getById(supplementId);
        try {
            tariff.deleteOption(second);
            for (SupplementEntity supplement : supplementService.getDependentSupplements(supplementId)) {
                tariff.deleteOption(supplement);
            }
            tariffRepository.save(tariff);
            log.info("Tariff " + tariff.getTitle() + " updated.");
        } catch (Exception e) {
            log.info("Error" + e.getMessage());
        }

        List<TariffJsonDto> tariffDtos = getTariffsWithCount();
        messageSender.sendMessage(tariffDtos);
    }
}
