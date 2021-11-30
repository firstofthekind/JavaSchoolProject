package com.firstofthekind.javaschoolproject;

import com.firstofthekind.javaschoolproject.dto.ContractDto;
import com.firstofthekind.javaschoolproject.entity.ClientEntity;
import com.firstofthekind.javaschoolproject.entity.ContractEntity;
import com.firstofthekind.javaschoolproject.entity.SupplementEntity;
import com.firstofthekind.javaschoolproject.entity.TariffEntity;
import com.firstofthekind.javaschoolproject.repository.ContractRepository;
import com.firstofthekind.javaschoolproject.repository.SupplementRepository;
import com.firstofthekind.javaschoolproject.service.ContractService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class ContractServiceTest {
    @Mock
    private ContractRepository contractRepository;
    @Mock
    private SupplementRepository supplementRepository;
    @InjectMocks
    private ContractService contractService;

    private ContractEntity contract;
    private ContractEntity anotherContract;
    ArrayList<ContractEntity> contractEntities;
    private SupplementEntity supplement;

    @BeforeEach
    public void setup() {
        contract = new ContractEntity();
        contract.setId(1L);
        anotherContract = new ContractEntity();
        anotherContract.setId(2L);
        ClientEntity client = new ClientEntity();
        client.setId(1L);
        contract.setClient(client);
        contractEntities = new ArrayList<>();
        contractEntities.add(contract);
        supplement = new SupplementEntity();
        supplement.setId(1L);
        supplement.setPrice(200);
        supplement.setConnectionCost(100);
    }

    @Test
    void shouldGetById() {
        when(contractRepository.getById(1L)).thenReturn(contract);
        ContractEntity found = contractService.getEntityById(1L);
        assertEquals(contract, found);
    }

    @Test
    void shouldNotGetById() {
        when(contractRepository.getById(2L)).thenReturn(anotherContract);
        ContractEntity found = contractService.getEntityById(2L);
        assertNotEquals(contract, found);
    }

    @Test
    void shouldGetByClientId() {
        when(contractRepository.getAllByClientId(1L)).thenReturn(contractEntities);
        List<ContractEntity> found = (ArrayList<ContractEntity>) contractService.getAllEntetiesByClientId(1L);
        assertEquals(contractEntities, found);
    }

    @Test
    void shouldNotGetByClientId() {
        when(contractRepository.getAllByClientId(2L)).thenReturn(null);
        List<ContractEntity> found = (ArrayList<ContractEntity>) contractService.getAllEntetiesByClientId(2L);
        assertNotEquals(contractEntities, found);
    }

}
