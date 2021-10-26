package com.firstofthekind.javaschoolproject.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "tariff")
@Getter
@Setter
@RequiredArgsConstructor
public class TariffEntity extends AbstractEntity {
    @NotBlank
    private String title;

    private double price;

    private double connectionCost;

    @ManyToMany
    @JoinTable(
            name = "tariff_supplement",
            joinColumns = @JoinColumn(name = "tariff_id"),
            inverseJoinColumns = @JoinColumn(name = "supplement_id")
    )
    private List<SupplementEntity> supplement = new LinkedList<>();

    public void addOption(SupplementEntity o) {
        supplement.add(o);
    }

    public void deleteOption(SupplementEntity o) {
        supplement.removeIf(option -> option.getId() == o.getId());
    }
}
