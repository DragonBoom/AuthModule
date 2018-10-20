package indi.auth.sql;

import java.io.Serializable;

import org.junit.jupiter.api.Test;

import indi.auth.entity.UserDO;

public class ClassTest {

    @Test
    void go() {
        System.out.println(Serializable.class.isAssignableFrom(UserDO.class));
    }
}
