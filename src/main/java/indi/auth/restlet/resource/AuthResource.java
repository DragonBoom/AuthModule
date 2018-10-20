package indi.auth.restlet.resource;

import org.apache.ibatis.session.SqlSession;
import org.restlet.data.Status;
import org.restlet.resource.Post;

import indi.auth.dao.UserDao;
import indi.auth.entity.SessionDO;
import indi.auth.entity.UserDO;
import indi.auth.service.Service;
import indi.auth.sql.SqlSessionManager;
import indi.core.rest.result.RestResult;

public class AuthResource extends MyResource {

    /**
     * 登陆
     */
    @Post("json")
    public RestResult<?> login(UserDO user) {
        RestResult<?> checkAuthAndGetSessionResult = Service.getSession(super.getRequest());
        if (checkAuthAndGetSessionResult.isError()) {
            return checkAuthAndGetSessionResult;
        }
        SessionDO session = (SessionDO) checkAuthAndGetSessionResult.getContent();
        try (SqlSession sqlSession = SqlSessionManager.getSession()) {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            UserDO trueUser = userDao.getById(user.getId());
            // TODO refresh sessionId
            if (trueUser.getPassword().equals(user.getPassword())) {
                session.setUserId(trueUser.getId());
                return RestResult.SUCCESS_RESULT;
            } else {
                return RestResult.asError(Status.CLIENT_ERROR_FORBIDDEN.getCode(), "密码错误");
            }
        }
    }
}
