package com.services.user.management.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;


@Data
@Setter(value = AccessLevel.NONE)
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @NotBlank
    private String name;
    @Min(value = 18, message="age should be at least {value}")
    private int age;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String phone;
    private List<String> socialMedia;
}
