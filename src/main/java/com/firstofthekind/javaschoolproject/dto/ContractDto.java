package com.firstofthekind.javaschoolproject.dto;


import lombok.Data;

import java.util.Set;

@Data
public class ContractDto {
    private long id;
    private String title;
    private double price;
    private double connectionCost;
    private TariffDto tariffDto;
    private Set<SupplementDto> supplementDtos;
    private  String number;
    private ClientDto clientDto;
}
