package indi.auth.restlet.resource;

import org.apache.ibatis.session.SqlSession;
import org.restlet.resource.Get;

import indi.auth.dao.UserDao;
import indi.auth.data.AuthInfo;
import indi.auth.entity.SessionDO;
import indi.auth.entity.UserDO;
import indi.auth.service.Service;
import indi.auth.sql.SqlSessionManager;
import indi.core.rest.result.RestResult;

public class InfoResource extends MyResource {

    /**
     * 获取权限信息
     */
    @Get
    public RestResult<?> getAuthInfo() {
        // check auth and get session
        RestResult<?> checkAuthAndGetSessionResult = Service.getSession(super.getRequest());
        if (checkAuthAndGetSessionResult.isError()) {
            return checkAuthAndGetSessionResult;
        }
        if (checkAuthAndGetSessionResult.getContent() == null) {
            
        }
        SessionDO session = (SessionDO) checkAuthAndGetSessionResult.getContent();
        
        try (SqlSession sqlSession = SqlSessionManager.getSession()) {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            UserDO user = userDao.getById(session.getUserId());
            return RestResult.asSuccess(AuthInfo.of(user, session));
        }
    }
}
