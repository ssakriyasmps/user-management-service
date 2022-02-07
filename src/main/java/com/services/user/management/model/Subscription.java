package com.services.user.management.model;

import lombok.*;

import java.util.List;


@Data
@Setter(value = AccessLevel.NONE)
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    private String userId;
    private List<String> socialMedia;
}
