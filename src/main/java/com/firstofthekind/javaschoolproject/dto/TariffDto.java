package com.firstofthekind.javaschoolproject.dto;


import lombok.Data;

import java.util.Set;

@Data
public class TariffDto {
    private long id;
    private String title;
    private double price;
    private double connectionCost;
    private Set<SupplementDto> supplementDtos;
}
