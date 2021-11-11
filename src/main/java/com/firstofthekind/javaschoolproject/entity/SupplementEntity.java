package com.firstofthekind.javaschoolproject.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "supplement")
@Getter
@Setter
@RequiredArgsConstructor
public class SupplementEntity extends AbstractEntity {

    @NotBlank
    private String title;

    private double price;

    private double connectionCost;

    @ManyToMany
    @JoinTable(
            name = "supplement_incompatible",
            joinColumns = @JoinColumn(name = "supplement_id"),
            inverseJoinColumns = @JoinColumn(name = "incompatible_supp_id")
    )
    private Set<SupplementEntity> incompatible = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "supplement_codependents",
            joinColumns = @JoinColumn(name = "supplement_id"),
            inverseJoinColumns = @JoinColumn(name = "codependents_supp_id")
    )
    private Set<SupplementEntity> codependents = new HashSet<>();

    public void addIncompatible(SupplementEntity sup) {
        incompatible.add(sup);
    }

    public void deleteIncompatible(SupplementEntity sup) {
        incompatible.removeIf(option1 -> option1.getId() == sup.getId());
    }

    public void addCodependent(SupplementEntity sup) {
        codependents.add(sup);
    }

    public void deleteCodependent(SupplementEntity sup) {
        codependents.removeIf(o -> o.getId() == sup.getId());
    }

    public boolean isCompatibleWith(Set<SupplementEntity> added) {
        if (this.incompatible.isEmpty()) {
            return true;
        }
        for (SupplementEntity sup :
                added) {
            if (sup.getIncompatible()
                    .stream()
                    .anyMatch(o -> o.getId() == this.getId())) {
                return false;
            }
        }
        return true;
    }

    public boolean isDependentFrom(Set<SupplementEntity> added) {
        if (this.codependents.isEmpty()) {
            return true;
        }
        for (SupplementEntity sup:
                this.getCodependents()) {
            if (added
                    .stream()
                    .noneMatch(o -> o.getId() == sup.getId())) {
                return false;
            }
        }
        return true;
    }
}
