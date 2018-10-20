package indi.auth.dao;

import java.util.List;

import org.junit.jupiter.api.Test;

import indi.auth.entity.UserDO;
import indi.auth.sql.SqlSessionManager;

class UserDaoTest {

    @Test
    void test() {
        List<UserDO> result = SqlSessionManager.getSession().getMapper(UserDao.class).query(1, 1);
        System.out.println(result);
    }

}
