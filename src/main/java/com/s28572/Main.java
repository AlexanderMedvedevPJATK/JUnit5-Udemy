package com.s28572;

import com.s28572.junit.dao.UserDao;
import lombok.Getter;

@Getter
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world");
        System.out.println(System.getProperty("skip"));
        System.out.println(System.getProperties().entrySet());
    }
}