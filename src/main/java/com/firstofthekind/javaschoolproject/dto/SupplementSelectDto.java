package com.firstofthekind.javaschoolproject.dto;


import com.firstofthekind.javaschoolproject.entity.SupplementEntity;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class SupplementSelectDto {
    private long id;
    private String title;
    private double price;
    private double connectionCost;
    private boolean isDeleted;


}
