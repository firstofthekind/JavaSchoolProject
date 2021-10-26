package com.firstofthekind.javaschoolproject.service;

import com.firstofthekind.javaschoolproject.dto.*;
import com.firstofthekind.javaschoolproject.entity.ContractEntity;
import com.firstofthekind.javaschoolproject.exception.CodependentSupplementException;
import com.firstofthekind.javaschoolproject.exception.IncompatibleSupplementException;
import com.firstofthekind.javaschoolproject.repository.ContractRepository;
import com.firstofthekind.javaschoolproject.repository.SupplementRepository;
import com.firstofthekind.javaschoolproject.repository.TariffRepository;
import com.firstofthekind.javaschoolproject.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class CartService {
    private final ContractRepository contractRepository;
    private final SupplementRepository supplementRepository;
    private final TariffRepository tariffRepository;
/*
    public void updateContract(CartDto cart) {
        ContractDto contractDto;
        ItemDto itemDto =
                cart.getItemDto();
        contractDto = itemDto.getContractDto();
        contractDto.setTariffDto(itemDto.getTariffDto());
        contractDto.setSupplementDto(itemDto.getSupplementDtoSet());
        contractDto.setPrice(itemDto.getPrice());
        contractDto.setConnectionCost(itemDto.getConnectionCost());
        contractRepository.save(ObjectMapperUtils.map(contractDto, ContractEntity.class));
        log.info("Contract with ID " + contractDto.getId() + " is updated");

       // cart.deleteItem();
    }*/


  /*  public void addTariff(CartDto cart, Long tariffId, Long contractId) {
        TariffDto tariff = ObjectMapperUtils.map(tariffRepository.getById(tariffId), TariffDto.class);
        ItemDto cartItem = cart.getByContract(contractId);
        if (cartItem == null) {
            cartItem = createItemDto(contractId);
            cart.addItem(cartItem);
            log.info("New cart item is created to add tariff");
        }
        cartItem.setTariffDto(tariff);
        cartItem.setPrice(updatePrice(cartItem));
        cartItem.setConnectionCost(updateConnectionCost(cartItem));
        log.info("Tariff is added to the cart");
    }*/
/*
    public void addTariff(CartDto cart, Long tariffId) {
        TariffDto tariff = ObjectMapperUtils.map(tariffRepository.getById(tariffId), TariffDto.class);
        ItemDto cartItem = cart.getItemDto();
        if (cartItem == null) {
            cartItem = createItemDto();
            cart.addItem(cartItem);
            log.info("New cart item is created to add tariff");
        }
        cartItem.setTariffDto(tariff);
        cartItem.setPrice(updatePrice(cartItem));
        cartItem.setConnectionCost(updateConnectionCost(cartItem));
        log.info("Tariff is added to the cart");
    }

    public  LinkedList<SupplementDto> getAvailableSupplements(CartDto cartDto) {
        ItemDto itemDto = cartDto.getItemDto();
        Set<SupplementDto> itemSupplements = getItemSupplements(itemDto);
         LinkedList<SupplementDto> availableSupplements = ObjectMapperUtils.mapAll(supplementRepository.findAll(), SupplementDto.class);
        for (SupplementDto supplementDto :
                itemSupplements) {
            availableSupplements.removeIf(o -> o.getId() == supplementDto.getId());
        }
        return availableSupplements;
    }*/
/*
    private  LinkedList<SupplementDto> getItemSupplements(ItemDto itemDto) {
         LinkedList<SupplementDto> cartItemOptions;
        if (itemDto != null) {
            cartItemOptions = new  LinkedList<>(itemDto.getSupplementDtoSet());
            cartItemOptions.addAll(itemDto.getTariffDto().getSupplementDtoSet());
        } else {
            ContractDto contractDto = new ContractDto();
            cartItemOptions = new  LinkedList<>(contractDto.getSupplementDto());
            TariffDto tariffDto = contractDto.getTariffDto();
            if (tariffDto != null) {
                cartItemOptions.addAll(tariffDto.getSupplementDtoSet());
            }
        }
        return cartItemOptions;
    }

    public void addSupplement(CartDto cartDto, Long supplementID) throws IncompatibleSupplementException,
            CodependentSupplementException {
        SupplementDto supplement = supplementRepository.getSupplementDtoById(supplementID);
        ItemDto itemDto = cartDto.getItemDto();
         LinkedList<SupplementDto> itemSupplements = getItemSupplements(itemDto);
        if (!supplement.isCompatible(itemSupplements)) {
            throw new IncompatibleSupplementException(supplement.getTitle() + " incompatible with current supplements");
        }
        if (!supplement.isCodependent(itemSupplements)) {
            throw new CodependentSupplementException(supplement.getTitle() + " has dependent supplements");
        }
        if (itemDto == null) {
            itemDto = createItemDto();
            cartDto.addItem(itemDto);
            log.info("New item created to add supplement " + supplement.getTitle());
        }
        itemDto.addSupplement(supplement);
        itemDto.setPrice(updatePrice(itemDto));
        itemDto.setConnectionCost(updateConnectionCost(itemDto));
        log.info("Supplement " + supplement.getTitle() + " was added");
    }

    public void deleteSupplement(CartDto cartDto, Long optionID) {
        SupplementDto supplementDto = supplementRepository.getSupplementDtoById(optionID);
        ItemDto itemDto = cartDto.getItemDto();
        itemDto.deleteSupplement(supplementDto);
        itemDto.setPrice(updatePrice(itemDto));
        itemDto.setConnectionCost(updateConnectionCost(itemDto));
        log.info("Supplement " + supplementDto.getTitle() + " deleted from the cart");
    }

    private ItemDto createItemDto() {
        ItemDto cartItem = new ItemDto();
        ContractDto contractDto = new ContractDto();
        TariffDto tariffDto = contractDto.getTariffDto();
        Set<SupplementDto> supplementDtos = new  LinkedList<>(contractDto.getSupplementDto());
        cartItem.setContractDto(contractDto);
        cartItem.setTariffDto(tariffDto);
        cartItem.setSupplementDtoSet(supplementDtos);
        return cartItem;
    }

    public void deleteItemDto(CartDto cartDto) {
        cartDto.deleteItem();
    }

    private double updatePrice(ItemDto itemDto) {
        double n = 0;
        TariffDto itemDtoTariff = itemDto.getTariffDto();
        Set<SupplementDto> supplementDtoSet = itemDto.getSupplementDtoSet();
        if (supplementDtoSet != null) {
            n += supplementDtoSet.stream().mapToDouble(SupplementDto::getPrice).sum();
        }
        if (itemDtoTariff != null) {
            n += itemDtoTariff.getPrice();
        }
        return n;
    }

    private double updateConnectionCost(ItemDto itemDto) {
        double n = 0;
        TariffDto tariffDto = itemDto.getTariffDto();
        Set<SupplementDto> options = itemDto.getSupplementDtoSet();
        if (options != null) {
            n += options.stream().mapToDouble(SupplementDto::getConnectionCost).sum();
        }
        if (tariffDto != null) {
            n += tariffDto.getSupplementDtoSet().stream().mapToDouble(SupplementDto::getConnectionCost).sum();
        }
        return n;
    }*/
}
