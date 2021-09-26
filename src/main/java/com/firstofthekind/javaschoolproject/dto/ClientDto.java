package com.firstofthekind.javaschoolproject.dto;


import lombok.Data;

import java.util.Set;

@Data
public class ClientDto {
    private long id;
    private String firstname;
    private String secondname;
    private Data birthdate;
    private String address;
    private String passport;
    private String email;
    private Set<ContractDto> contractDtos;
}
