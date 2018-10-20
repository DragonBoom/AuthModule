package indi.auth.entity;

import org.junit.jupiter.api.Test;

import indi.auth.data.AuthInfo;

class AuthInfoTest {

    @Test
    void test() {
        UserDO user = new UserDO();
        user.setAvatar("123");
        SessionDO session = new SessionDO();
        session.setUserId(123L);
        System.out.println(AuthInfo.of(user, session));
    }

}
