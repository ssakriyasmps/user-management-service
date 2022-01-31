package com.services.user.management.repository;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="User")
@Data
@Setter(value = AccessLevel.NONE)
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    // @Id annotation specifies the primary key of an entity.
    // @GeneratedValue provides the generation strategy
    // specification for the primary key values.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int age;
    private String email;
    private String phone;

    public UserEntity(String name, int age, String email, String phone){
        this.name = name;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }
}
