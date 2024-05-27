package com.s28572.junit.dto;

import lombok.Value;

@Value(staticConstructor = "of")
public class User {
    int id;
    String username;
    String password;
}
