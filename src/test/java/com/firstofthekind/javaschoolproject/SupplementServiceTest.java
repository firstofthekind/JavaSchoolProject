package com.firstofthekind.javaschoolproject;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.firstofthekind.javaschoolproject.entity.ContractEntity;
import com.firstofthekind.javaschoolproject.entity.SupplementEntity;
import com.firstofthekind.javaschoolproject.entity.TariffEntity;
import com.firstofthekind.javaschoolproject.exception.CodependentSupplementException;
import com.firstofthekind.javaschoolproject.repository.SupplementRepository;
import com.firstofthekind.javaschoolproject.service.SupplementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class SupplementServiceTest {

    @Mock
    private SupplementRepository supplementRepository;

    @InjectMocks
    private SupplementService supplementService;

    private SupplementEntity supplement;
    private SupplementEntity anotherSupplement;
    private List<SupplementEntity> supplementEntities;

    @BeforeEach
    public void setup() {
        supplement = new SupplementEntity();
        supplement.setId(1L);
        anotherSupplement = new SupplementEntity();
        anotherSupplement.setId(2L);
        supplementEntities = new LinkedList<>();
        supplementEntities.add(supplement);
        supplementEntities.add(anotherSupplement);

        TariffEntity tariff = new TariffEntity();
        tariff.addOption(anotherSupplement);


        ContractEntity contract = new ContractEntity();
        contract.addOption(supplement);
        contract.setTariff(tariff);
    }

    @Test
    void shouldGetById() {
        when(supplementRepository.getById(1L)).thenReturn(supplement);
        SupplementEntity found = supplementService.getOne(1L);
        assertEquals(supplement, found);
    }

    @Test
    void shouldNotGetById() {
        when(supplementRepository.getById(1L)).thenReturn(anotherSupplement);
        SupplementEntity found = supplementService.getOne(1L);
        assertNotEquals(supplement, found);
    }

    @Test
    void shouldGetIncompatibles() {
        when(supplementRepository.getById(1L)).thenReturn(supplement);
        supplement.addIncompatible(anotherSupplement);
        Set<SupplementEntity> foundIncompatibles = supplementService.getIncompatible(supplement.getId());
        assertEquals(supplement.getIncompatible(), foundIncompatibles);
        supplement.deleteIncompatible(anotherSupplement);
    }

    @Test
    void shouldGetCompatibles() {
        when(supplementRepository.getById(1L)).thenReturn(supplement);
        when(supplementRepository.findAll()).thenReturn((LinkedList<SupplementEntity>) supplementEntities);
        List<SupplementEntity> compatibles = new LinkedList<>();
        compatibles.add(anotherSupplement);
        List<SupplementEntity> foundCompatibles = supplementService.getCompatible(supplement.getId());
        assertEquals(compatibles, foundCompatibles);
    }

    @Test
    void shouldNotGetCompatibles() {
        when(supplementRepository.getById(1L)).thenReturn(supplement);
        when(supplementRepository.findAll()).thenReturn((LinkedList<SupplementEntity>) supplementEntities);
        List<SupplementEntity> compatibles = new LinkedList<>();
        compatibles.add(supplement);
        List<SupplementEntity> foundCompatibles = supplementService.getCompatible(supplement.getId());
        assertNotEquals(compatibles, foundCompatibles);
    }

    @Test
    void shouldGetDependent() {
        when(supplementRepository.getById(1L)).thenReturn(supplement);
        supplement.addCodependent(anotherSupplement);
        Set<SupplementEntity> expected = new HashSet<>();
        expected.add(anotherSupplement);
        Set<SupplementEntity> found = supplementService.getDependentSupplements(1L);
        assertEquals(expected, found);
        supplement.deleteCodependent(anotherSupplement);
    }

    @Test
    void shouldNotGetDependent() {
        when(supplementRepository.getById(1L)).thenReturn(supplement);
        Set<SupplementEntity> unexpected = new HashSet<>();
        unexpected.add(supplement);
        Set<SupplementEntity> found = supplementService.getDependentSupplements(1L);
        assertNotEquals(unexpected, found);
    }

    @Test
    void shouldMakeSupplementsIncompatible() throws CodependentSupplementException {
        when(supplementRepository.getById(1L)).thenReturn(supplement);
        when(supplementRepository.getById(2L)).thenReturn(anotherSupplement);
        doReturn(null).when(supplementRepository).save(isA(SupplementEntity.class));
        Set<SupplementEntity> expected = new HashSet<>();
        expected.add(anotherSupplement);
        supplementService.addIncompatible(supplement.getId(), anotherSupplement.getId());
        assertEquals(expected, supplement.getIncompatible());
    }

    @Test
    void shouldNotMakeSupplementsIncompatible() throws CodependentSupplementException {
        supplement.addCodependent(anotherSupplement);
        when(supplementRepository.getById(1L)).thenReturn(supplement);
        when(supplementRepository.getById(2L)).thenReturn(anotherSupplement);
        assertThrows(CodependentSupplementException.class, () -> supplementService.addIncompatible(supplement.getId(), anotherSupplement.getId()));
        supplement.deleteCodependent(anotherSupplement);
    }
}
