package indi.auth.restlet.resource;

import java.util.Random;

import org.apache.ibatis.session.SqlSession;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import indi.auth.dao.UserDao;
import indi.auth.data.UserDTO;
import indi.auth.entity.SessionDO;
import indi.auth.entity.UserDO;
import indi.auth.service.Service;
import indi.auth.sql.SqlSessionManager;
import indi.core.rest.result.RestResult;

public class UserResource extends MyResource {

    /**
     * 根据userId与auth字段，先判断权限，再查找User
     */
    @Get
    public RestResult<?> getAuthUser() {
        // check auth
        RestResult<?> checkAuthAndGetSessionResult = Service.getSession(super.getRequest());
        if (checkAuthAndGetSessionResult.isError()) {
            return checkAuthAndGetSessionResult;
        }

        Long userId = null;
        try {
            userId = Long.parseLong(super.getQueryValue("id"));
        } catch (NumberFormatException e) {
            return RestResult.asError(Status.CLIENT_ERROR_BAD_REQUEST.getCode(), "参数格式错误");
        }
        UserDO user = null;
        try (SqlSession session = SqlSessionManager.getSession()) {
            user = session.getMapper(UserDao.class).getById(userId);
        }

        if (user == null) {
            return RestResult.asError(Status.CLIENT_ERROR_NOT_FOUND.getCode());
        }
        return RestResult.asSuccess(UserDTO.of(user)); // TODO
    }

    /**
     * 注册，创建用户
     */
    @Post("json")
    public RestResult<?> createAuthUser(UserDO user) {
        if (user == null) {
            return RestResult.asError(Status.CLIENT_ERROR_BAD_REQUEST.getCode());
        }

        SqlSession sqlSession = SqlSessionManager.getSession();

        RestResult<?> createSessionResult = Service.createSession(super.getRequest(), super.getResponse());
        SessionDO session = (SessionDO) createSessionResult.getContent();
        
        if (session == null) {
            return RestResult.asError(Status.CLIENT_ERROR_NOT_FOUND.getCode(), "session not exist");
        }
        if (session.getUserId() != null) {
            sqlSession.commit();
            return RestResult.asError(Status.CLIENT_ERROR_BAD_REQUEST.getCode(), "已登录状态无法创建用户");
        }

        // 创建用户
        // build userId
        Long userId = (long) new Random().nextInt(429496729);
        System.out.println(userId);
        // TODO
        user.setId(userId);
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        userDao.add(user);

        sqlSession.commit();
        return RestResult.SUCCESS_RESULT;
    }
}
