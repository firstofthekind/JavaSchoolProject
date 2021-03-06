package com.firstofthekind.javaschoolproject.dto;


import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class TariffDto {
    private long id;
    private String title;
    private double price;
    private double connectionCost;
    private List<SupplementDto> supplementDtoSet;
    private boolean isDeleted;
    private long count;
}
