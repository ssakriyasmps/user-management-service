package com.services.user.management.repository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="User")
@Data
@Setter(value = AccessLevel.NONE)
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    // @Id annotation specifies the primary key of an entity.
    // @GeneratedValue provides the generation strategy
//    // specification for the primary key values.
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @GenericGenerator(
            name = "user_seq",
            strategy = "com.services.user.management.repository.UserSequenceIdGenerator",
            parameters = {
                    @Parameter(name = UserSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%019d"),
                    @Parameter(name = UserSequenceIdGenerator.VALUE_SUFFIX_PARAMETER, value = "00") })
    private String id;
    private String name;
    private int age;
    private String email;
    private String phone;
    @Transient
    private String region="001";

    public UserEntity(String name, int age, String email, String phone){
        this.name = name;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }

}
