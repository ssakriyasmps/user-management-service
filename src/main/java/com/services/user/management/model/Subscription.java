package com.services.user.management.model;

import lombok.*;

import java.util.List;


@Data
@Setter(value = AccessLevel.NONE)
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    private int userId;
    private List<String> socialMedia;
}
