package com.firstofthekind.javaschoolproject.dto;


import com.firstofthekind.javaschoolproject.entity.SupplementEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Data
public class SupplementDto {
    private long id;
    private String title;
    private double price;
    private double connectionCost;
    private Set<SupplementDto> incompatible;
    private Set<SupplementDto> codependents;
    private boolean isDeleted;


    public boolean isCompatible(HashSet<SupplementDto> supplementDtos) {
        if (this.incompatible.isEmpty()) {
            return true;
        }
        for (SupplementDto supplementDto :
                supplementDtos) {
            if (supplementDto.getIncompatible()
                    .stream()
                    .anyMatch(o -> o.getId() == this.getId())) {
                return false;
            }
        }
        return true;
    }
    public boolean isCodependent(HashSet<SupplementDto> supplementDtos) {
        if (this.codependents.isEmpty()) {
            return true;
        }
        for (SupplementDto supplementDto :
                supplementDtos) {
            if (supplementDto.getCodependents()
                    .stream()
                    .anyMatch(o -> o.getId() == this.getId())) {
                return true;
            }
        }
        return false;
    }
}
