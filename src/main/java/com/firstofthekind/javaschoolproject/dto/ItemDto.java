package com.firstofthekind.javaschoolproject.dto;


import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class ItemDto {
    private double price;
    private double connectionCost;
    private ContractDto contractDto;
    private TariffDto tariffDto;
    private Set<SupplementDto> supplementDtoSet;

    public ItemDto(TariffDto one) {
        this.tariffDto = one;
    }

    public ItemDto(TariffDto tariffDto, Set<SupplementDto> supplementDtoSet) {
        this.tariffDto = tariffDto;
        this.supplementDtoSet = supplementDtoSet;
    }

    public ItemDto(ContractDto contractDto, TariffDto tariffDto, Set<SupplementDto> supplementDtoSet) {
        this.contractDto = contractDto;
        this.tariffDto = tariffDto;
        this.supplementDtoSet = supplementDtoSet;
    }

    public void addSupplement(SupplementDto supplementDto) {
        supplementDtoSet.add(supplementDto);
    }

    public void deleteSupplement(SupplementDto supplementDto) {
        supplementDtoSet.removeIf(sup -> sup.getId() == supplementDto.getId());
    }

}
