package com.services.user.management.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;


@Data
@Setter(value = AccessLevel.NONE)
@AllArgsConstructor
@NoArgsConstructor
public class UserExt {
    @NotBlank
    private String name;
    @Min(value = 18, message="age should be at least {value}")
    private int age;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @FutureOrPresent
    private LocalDate dob;
}
