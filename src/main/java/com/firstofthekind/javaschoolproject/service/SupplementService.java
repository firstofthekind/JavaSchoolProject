package com.firstofthekind.javaschoolproject.service;


import com.firstofthekind.javaschoolproject.dto.SupplementDto;
import com.firstofthekind.javaschoolproject.dto.SupplementSelectDto;
import com.firstofthekind.javaschoolproject.entity.SupplementEntity;
import com.firstofthekind.javaschoolproject.entity.TariffEntity;
import com.firstofthekind.javaschoolproject.exception.CodependentSupplementException;
import com.firstofthekind.javaschoolproject.exception.IncompatibleSupplementException;
import com.firstofthekind.javaschoolproject.repository.SupplementRepository;
import com.firstofthekind.javaschoolproject.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.support.ManagedSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j2
public class SupplementService {
    private final SupplementRepository supplementRepository;

    @Transactional
    public Iterable<SupplementDto> getAll() {
        return ObjectMapperUtils.mapAll(supplementRepository.findAll(), SupplementDto.class);
    }

    @Transactional
    public LinkedList<SupplementSelectDto> getAllSelect() {
        return ObjectMapperUtils.mapAll(supplementRepository.findAll(), SupplementSelectDto.class);
    }

    @Transactional
    public SupplementEntity getOne(long id) {
        return supplementRepository.getById(id);
    }

    @Transactional
    public SupplementDto getById(long id) {
        return ObjectMapperUtils.map(supplementRepository.getById(id), SupplementDto.class);
    }

    @Transactional
    public void save(SupplementDto supplementDto) {
        supplementRepository.save(ObjectMapperUtils.map(supplementDto, SupplementEntity.class));
        log.info("Supplement " + supplementDto.getTitle() + " created.");
    }

    @Transactional
    public void setDeleted(long id, boolean b) {
        SupplementDto supDto = getById(id);
        supDto.setDeleted(b);
        save(supDto);
        log.info("supplement's status with id " + supDto.getId() + " was updated");
    }

    @Transactional
    void updateSupplementDto(SupplementDto supplementDto) {
        supplementRepository.save(ObjectMapperUtils.map(supplementDto, SupplementEntity.class));
    }

    @Transactional
    public LinkedList<SupplementDto> getAllAvailableToTariff(TariffEntity tariff) {
        List<SupplementEntity> tariffSupplements = tariff.getSupplement();
        LinkedList<SupplementEntity> allSupplements = supplementRepository.findAll();
        for (SupplementEntity supplement : tariffSupplements) {
            allSupplements.removeIf(s -> s.getId() == supplement.getId());
        }
        return ObjectMapperUtils.mapAll(allSupplements, SupplementDto.class);
    }

    @Transactional
    public void addIncompatible(long firstSupId,
                                long secondSupId) throws CodependentSupplementException {
        SupplementEntity first = supplementRepository.getById(firstSupId);
        SupplementEntity second = supplementRepository.getById(secondSupId);
        if (first.getCodependents().stream().noneMatch(o -> o.getId() == second.getId()) &&
                second.getCodependents().stream().noneMatch(o -> o.getId() == first.getId())) {
            first.addIncompatible(second);
            second.addIncompatible(first);
            supplementRepository.save(first);
            supplementRepository.save(second);
            log.info("Supplement " + first.getTitle() + " and " + second.getTitle() + " set as incompatible");
        } else {
            log.info("Error. Supplements " + first.getTitle() + " and " +
                    second.getTitle() + " are codependent. ");
            throw new CodependentSupplementException(
                    "Error. Supplements " + first.getTitle() + " and " +
                            second.getTitle() + " are codependent.");
        }
    }

    @Transactional
    public void deleteIncompatible(long firstSupId,
                                   long secondSupId) {
        SupplementEntity first = supplementRepository.getById(firstSupId);
        SupplementEntity second = supplementRepository.getById(secondSupId);
        first.deleteIncompatible(second);
        second.deleteIncompatible(first);
        supplementRepository.save(first);
        supplementRepository.save(second);
        log.info("Supplements " + first.getTitle() + " and " + second.getTitle() + " now is compatible");
    }

    @Transactional
    public Set<SupplementEntity> getIncompatible(long SupplementId) {
        SupplementEntity Supplement = supplementRepository.getById(SupplementId);
        return Supplement.getIncompatible();
    }

    @Transactional
    public Set<SupplementSelectDto> getIncompatibleForAll(Iterable<SupplementSelectDto> selectDtos) {
        Set<SupplementSelectDto> incompatible = new HashSet<>();
        for (SupplementSelectDto dto : selectDtos) {
            incompatible.addAll(ObjectMapperUtils.mapAll(getIncompatible(dto.getId()), SupplementSelectDto.class));
            for (SupplementEntity entity : getDependentSupplements(dto.getId())) {
                incompatible.addAll(ObjectMapperUtils.mapAll(getIncompatible(entity.getId()), SupplementSelectDto.class));
            }
            for (SupplementEntity entity : getIncompatible(dto.getId())) {
                incompatible.addAll(ObjectMapperUtils.mapAll(getDependentSupplements(entity.getId()), SupplementSelectDto.class));
            }
        }
        System.out.println(incompatible);
        return incompatible;
    }

    @Transactional
    public LinkedList<SupplementEntity> getCompatible(long SupplementId) {
        LinkedList<SupplementEntity> compatible = supplementRepository.findAll();
        Set<SupplementEntity> incompatible = supplementRepository.getById(SupplementId).getIncompatible();
        if (incompatible != null) {
            for (SupplementEntity o :
                    incompatible) {
                long supId = o.getId();
                compatible.removeIf(Supplement -> Supplement.getId() == supId);
            }
        }
        compatible.removeIf(o -> o.getId() == SupplementId);
        return compatible;
    }

    @Transactional
    public void addDependent(long firstSupplementId,
                             long secondSupplementId) throws IncompatibleSupplementException {
        SupplementEntity first = supplementRepository.getById(firstSupplementId);
        SupplementEntity second = supplementRepository.getById(secondSupplementId);
        if (first.getIncompatible().stream().noneMatch(o -> o.getId() == second.getId())) {
            first.addCodependent(second);
            supplementRepository.save(first);
            second.addCodependent(first);
            supplementRepository.save(second);
            log.info("Supplement " + first.getTitle() + " codependent with " + second.getTitle());
        } else {
            log.info("Error. Supplement " + first.getTitle() + " is incompatible with "
                    + second.getTitle() + " and can't be codependent");
            throw new IncompatibleSupplementException(
                    "Error. Supplement " + first.getTitle() + " is incompatible with "
                            + second.getTitle() + " and can't be codependent");
        }
    }

    @Transactional
    public void deleteDependent(long firstSupplementId,
                                long secondSupplementId) {
        SupplementEntity first = supplementRepository.getById(firstSupplementId);
        SupplementEntity second = supplementRepository.getById(secondSupplementId);
        first.deleteCodependent(second);
        supplementRepository.save(first);
        second.deleteCodependent(first);
        supplementRepository.save(second);
        log.info("Error. Supplement " + first.getTitle() + " is not codependent with " + second.getTitle());
    }

    @Transactional
    public Set<SupplementEntity> getDependentSupplements(long supplementId) {
        SupplementEntity supplement = supplementRepository.getById(supplementId);
        return supplement.getCodependents();
    }

    @Transactional
    public List<SupplementEntity> getIndependentSupplements(long supplementId) {
        List<SupplementEntity> independent = supplementRepository.findAll();
        Set<SupplementEntity> codependent = supplementRepository.getById(supplementId).getCodependents();
        for (SupplementEntity supplement :
                codependent) {
            independent.removeIf(o -> o.getId() == supplement.getId());
        }
        independent.removeIf(o -> o.getId() == supplementId);
        return independent;
    }

    @Transactional
    public List<SupplementEntity> getAvailableSupplements(long supplementId) {
        List<SupplementEntity> independent = getIndependentSupplements(supplementId);
        Set<SupplementEntity> codependent = getIncompatible(supplementId);
        for (SupplementEntity supplement :
                codependent) {
            independent.removeIf(o -> o.getId() == supplement.getId());
        }
        independent.removeIf(o -> o.getId() == supplementId);
        return independent;
    }

}
