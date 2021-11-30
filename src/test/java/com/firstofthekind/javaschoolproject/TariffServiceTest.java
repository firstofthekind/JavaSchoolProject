package com.firstofthekind.javaschoolproject;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.firstofthekind.javaschoolproject.entity.TariffEntity;
import com.firstofthekind.javaschoolproject.repository.TariffRepository;
import com.firstofthekind.javaschoolproject.service.TariffService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TariffServiceTest {

    @Mock
    private TariffRepository tariffDao;

    @InjectMocks
    private TariffService tariffService;

    private TariffEntity tariff;
    private TariffEntity anotherTariff;
    private List<TariffEntity> tariffs;
    private List<TariffEntity> notAddedToContractTariffs;

    @BeforeEach
    public void setup() {
        tariff = new TariffEntity();
        tariff.setId(1L);
        anotherTariff = new TariffEntity();
        anotherTariff.setId(2L);
        tariffs = new LinkedList<>();
        tariffs.add(anotherTariff);
        tariffs.add(tariff);
        notAddedToContractTariffs = new LinkedList<>();
        notAddedToContractTariffs.add(anotherTariff);
    }

    @Test
    void shouldGetAllTariffs() {
        when(tariffDao.findAll()).thenReturn(null);
        tariffService.getAllEnteties();
        verify(tariffDao).findAll();
    }

    @Test
    void shouldGetById() {
        when(tariffDao.getById(1L)).thenReturn(tariff);
        TariffEntity founded = tariffService.getOne(1L);
        assertEquals(founded, tariff);
    }

    @Test
    void shouldNotGetById() {
        when(tariffDao.getById(1L)).thenReturn(anotherTariff);
        TariffEntity founded = tariffService.getOne(1L);
        assertNotEquals(founded, tariff);
    }

    @Test
    void shouldGetNotAddedToContractTariffs() {
        when(tariffDao.findAll()).thenReturn((LinkedList<TariffEntity>) tariffs);
        List<TariffEntity> notContractTariffs = tariffService.getNotAddedToContractTariffs(tariff.getId());
        assertEquals(notContractTariffs, notAddedToContractTariffs);
    }

    @Test
    void shouldNotGetNotAddedToContractTariffs() {
        when(tariffDao.findAll()).thenReturn((LinkedList<TariffEntity>) tariffs);
        List<TariffEntity> notContractTariffs = tariffService.getNotAddedToContractTariffs(anotherTariff.getId());
        assertNotEquals(notContractTariffs, notAddedToContractTariffs);
    }
}
