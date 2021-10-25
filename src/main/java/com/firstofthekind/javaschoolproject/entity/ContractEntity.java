package com.firstofthekind.javaschoolproject.entity;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "contract")
@Data
@RequiredArgsConstructor
public class ContractEntity extends AbstractEntity {
    @NotBlank
    private String number;

    private boolean isBlockedByClient;

    private boolean isBlockedByAdmin;

    private double price;

    private double connectionCost;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @ManyToOne
    @JoinTable(name = "contract_tariff",
            joinColumns = @JoinColumn(name = "contract_id"),
            inverseJoinColumns = @JoinColumn(name = "tariff_id"))
    private TariffEntity tariff;

    @ManyToMany
    @JoinTable(name = "contract_supplement",
            joinColumns = @JoinColumn(name = "contract_id"),
            inverseJoinColumns = @JoinColumn(name = "supplement_id"))
    private Set<SupplementEntity> supplement = new HashSet<>();

    public ContractEntity(String number, double price,
                          double connectionCost, ClientEntity client,
                          TariffEntity tariffEntity, Set<SupplementEntity> supplementSet) {
        this.number = number;
        this.price = price;
        this.supplement = supplementSet;
        this.tariff = tariffEntity;
        this.connectionCost = connectionCost;
        this.client = client;
    }

    public void addOption(SupplementEntity supp) {
        this.supplement.add(supp);
    }

    public boolean deleteOption(SupplementEntity supp) {
        return this.supplement.removeIf(it -> it.getId() == supp.getId());
    }

}
