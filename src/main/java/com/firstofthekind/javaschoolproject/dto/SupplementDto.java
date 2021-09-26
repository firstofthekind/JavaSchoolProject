package com.firstofthekind.javaschoolproject.dto;


import lombok.Data;

@Data
public class SupplementDto {
    private long id;
    private String title;
    private double price;
    private double connectionCost;
}
