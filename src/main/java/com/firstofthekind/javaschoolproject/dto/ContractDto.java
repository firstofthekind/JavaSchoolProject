package com.firstofthekind.javaschoolproject.dto;


import com.firstofthekind.javaschoolproject.entity.ClientEntity;
import com.firstofthekind.javaschoolproject.entity.SupplementEntity;
import com.firstofthekind.javaschoolproject.entity.TariffEntity;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ContractDto {
    private long id;
    private String number;
    private double price;
    private double connectionCost;
    private ClientEntity client;
    private TariffEntity tariff;
    private Set<SupplementEntity> supplement;
    private boolean isDeleted;

    private boolean isBlockedByClient ;

    private boolean isBlockedByAdmin ;
}
