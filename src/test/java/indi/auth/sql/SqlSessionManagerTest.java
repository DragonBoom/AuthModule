package indi.auth.sql;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import indi.auth.dao.UserDao;
import indi.auth.entity.UserDO;

class SqlSessionManagerTest {

    @Test
    void test() {
//        SqlSessionManager sqlSessionManager = new SqlSessionManager();
        UserDO userDO = new UserDO();
        userDO.setId(1233L);
        userDO.setPassword("123");
        userDO.setUsername("1233");
        SqlSession session = SqlSessionManager.getSession();
        session.getMapper(UserDao.class).add(userDO);
        session.commit();
        System.out.println();
    }

}
