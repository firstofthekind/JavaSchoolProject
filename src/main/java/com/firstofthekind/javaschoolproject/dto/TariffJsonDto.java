package com.firstofthekind.javaschoolproject.dto;


import lombok.Data;

import java.util.List;

@Data
public class TariffJsonDto {
    private long id;
    private String title;
    private double price;
    private double connectionCost;
    private List<SupplementSelectDto> supplementDtoSet;
    private boolean isDeleted;
    private long count;
}
