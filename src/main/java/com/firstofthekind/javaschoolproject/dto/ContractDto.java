package com.firstofthekind.javaschoolproject.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@Setter
@Getter
@Data
public class ContractDto {
    private long id;
    private String number;
    private double price;
    private double connectionCost;
    private ClientDto clientDto;
    private TariffDto tariffDto;
    private Set<SupplementDto> supplementDto;
}
