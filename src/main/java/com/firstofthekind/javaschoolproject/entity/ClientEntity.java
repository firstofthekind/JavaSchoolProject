package com.firstofthekind.javaschoolproject.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "client")
@Getter
@Setter
@RequiredArgsConstructor
public class ClientEntity extends AbstractEntity{

    @NotBlank
    private String firstname;

    @NotBlank
    private String password;

    @NotBlank
    private String lastname;

    @OneToMany(mappedBy = "client")
    private Set<ContractEntity> contracts;

    @NotBlank
    private String birthdate;

    @NotBlank
    @Pattern(regexp = "\\d{4}\\s\\d{6}", message = "Enter passport in format 1234 123456")
    private String passport;

    @NotBlank
    private String address;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "client_role",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();

    public ClientEntity(String firstname, String lastname, String birthdate, String passport, String address, String email, String password) {
        this.firstname=firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.passport = passport;
        this.address=address;
        this.email=email;
        this.password = password;

    }

    public ClientEntity(String firstname, String encode) {
        this.firstname = firstname;
        this.password = encode;
    }
}
