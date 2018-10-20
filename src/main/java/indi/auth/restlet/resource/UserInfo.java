package indi.auth.restlet.resource;

import org.apache.ibatis.session.SqlSession;
import org.restlet.resource.Get;

import indi.auth.dao.UserDao;
import indi.auth.service.Service;
import indi.auth.sql.SqlSessionManager;
import indi.core.rest.result.RestResult;

public class UserInfo extends MyResource {

    @Get("?count")// 只能匹配 API + "?count"...
    public RestResult<?> getUserCount() {
        RestResult<?> getSessionResult = Service.getSession(this.getRequest());
        if (getSessionResult.isError()) {
            return getSessionResult;
        }
        try (SqlSession sqlSession = SqlSessionManager.getSession()) {
           UserDao userDao = sqlSession.getMapper(UserDao.class);
        return RestResult.asSuccess(userDao.getCount()); 
        }
    }
}
