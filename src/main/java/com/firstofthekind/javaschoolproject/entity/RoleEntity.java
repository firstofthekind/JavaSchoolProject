package com.firstofthekind.javaschoolproject.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "role")
@Getter
@Setter
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @Enumerated(EnumType.STRING)
    private ERole name;

    public RoleEntity(ERole name) {
        this.name = name;
    }

    public RoleEntity() {

    }

    public String getShortNames(List<RoleEntity> roleEntitySet) {
        StringBuilder list = new StringBuilder();
        for (RoleEntity role : roleEntitySet
        ) {
            list.append(role.getShortName()).append(", ");
        }
        return list.toString();
    }

    public String getShortName() {
        return name.toString().substring(5);
    }
}
