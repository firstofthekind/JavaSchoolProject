package com.firstofthekind.javaschoolproject.dto;


import com.firstofthekind.javaschoolproject.entity.RoleEntity;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class ClientDto {
    private long id;
    private String firstname;
    private String lastname;
    private String birthdate;
    private String address;
    private String passport;
    private String email;
    private Set<ContractDto> contractDtos;
    private Set<RoleEntity> roles;
    private Set<String> rolesStr;
    private boolean isDeleted;
    private String getShortNames() {
        StringBuilder list = new StringBuilder();
        for (RoleEntity role : roles
        ) {
            list.append(role.getShortName()).append(", ");
        }
        return list.toString();
    }
}
