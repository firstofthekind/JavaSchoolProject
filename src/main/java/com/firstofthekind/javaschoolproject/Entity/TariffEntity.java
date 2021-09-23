package com.firstofthekind.javaschoolproject.Entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tariff")
@Getter
@Setter
@RequiredArgsConstructor
public class TariffEntity extends AbstractEntity {
    @NotBlank
    private String title;

    private double price;

    @ManyToMany
    @JoinTable(
            name = "supplement_tariff",
            joinColumns = @JoinColumn(name = "tariff_id"),
            inverseJoinColumns = @JoinColumn(name = "supplement_id")
    )
    private Set<SupplementEntity> supplement = new HashSet<>();

    public void addOption(SupplementEntity o) {
        supplement.add(o);
    }

    public void deleteOption(SupplementEntity o) {
        supplement.removeIf(option -> option.getId() == o.getId());
    }
}
