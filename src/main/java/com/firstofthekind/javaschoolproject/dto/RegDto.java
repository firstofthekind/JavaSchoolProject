package com.firstofthekind.javaschoolproject.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Setter
@Getter
@Data
public class RegDto {
    @NotBlank
    private String firstname;

    @NotBlank
    private String password;

    @NotBlank
    private String passwordConfirm;

    @NotBlank
    private String birthdate;

    @NotBlank
    private String lastname;

    @NotBlank
    @Pattern(regexp = "\\d{4}\\s\\d{6}", message = "Enter passport in format 1234 123456")
    private String passport;

    @NotBlank
    private String address;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Email
    private String emailConfirm;

    private boolean checkbox;

    private Set<String> role;


}
