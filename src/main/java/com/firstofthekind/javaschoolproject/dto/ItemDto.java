package com.firstofthekind.javaschoolproject.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

@Getter
@Setter
public class ItemDto {
    private double price;
    private double connectionCost;
    private ContractDto contractDto;
    private TariffDto tariffDto;
    private HashSet<SupplementDto> supplementDtoSet;

    public void addSupplement(SupplementDto supplementDto) {
        supplementDtoSet.add(supplementDto);
    }

    public void deleteSupplement(SupplementDto supplementDto) {
        for (SupplementDto sup : supplementDtoSet) {
            if (sup.getId() == supplementDto.getId()) {
                supplementDtoSet.remove(sup);
            }
        }
    }

}
