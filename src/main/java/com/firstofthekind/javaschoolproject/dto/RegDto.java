package com.firstofthekind.javaschoolproject.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Data
public class RegDto {
    @NotEmpty
    private String firstname;

    @NotEmpty
    private String password;

    @NotEmpty
    private String passwordConfirm;

    @NotEmpty
    private String birthdate;

    @NotEmpty
    private String lastname;

    @NotEmpty
    @Pattern(regexp = "\\d{4}\\s\\d{6}", message = "Enter passport in format 1234 123456")
    private String passport;

    @NotEmpty
    private String address;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Email
    private String emailConfirm;

    @AssertTrue(message = "You should be agree with licence terms")
    private boolean checkbox;

    private List<String> role;



}
