package com.firstofthekind.javaschoolproject.Entity;


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
public class SupplementEntity extends AbstractEntity{

    @NotBlank
    private String title;

    @NotBlank
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
}
