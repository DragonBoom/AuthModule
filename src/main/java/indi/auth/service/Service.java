package indi.auth.service;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.ibatis.session.SqlSession;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Status;
import org.springframework.util.StringUtils;

import indi.auth.dao.SessionDao;
import indi.auth.entity.SessionDO;
import indi.auth.sql.SqlSessionManager;
import indi.core.rest.result.RestResult;
import indi.core.util.LoggerUtils;

public class Service {
    private static final Integer DEFAULT_TIMEOUT_FIELD = Calendar.DATE;
    private static final Integer DEFAULT_TIMEOUT_DURATION = 15;

    private static final AtomicInteger INCREASE_SEQ = new AtomicInteger(-1);

    /**
     * 创建会话，若已存在会话则删除旧会话并创建新的会话
     */
    public static RestResult<?> createSession(Request request, Response response) {
        try (SqlSession sqlSession = SqlSessionManager.getSession()) {
            SessionDao sessionDao = sqlSession.getMapper(SessionDao.class);
            if (getAuthKey(request) != null) {// 若已存在会话
                // 删除旧会话
                RestResult<?> checkAuthAndGetSessionResult = getSession(request);
                if (checkAuthAndGetSessionResult.isError()) {
                    return checkAuthAndGetSessionResult;
                }
                if (!checkAuthAndGetSessionResult.isError() && !(checkAuthAndGetSessionResult.getContent() == null)) {
                    SessionDO oldSession = (SessionDO) checkAuthAndGetSessionResult.getContent();
                    sessionDao.deleteBySessionId(oldSession.getSessionId());
                    LoggerUtils.getLogger().info("删除旧会话, id:{}", oldSession.getSessionId());
                }
            }
            // logic for create session...
            SessionDO session = new SessionDO();
            // 用自增序列实现session id不重复
            Integer seq = 0;
            while (INCREASE_SEQ.get() == -1) {
                if (INCREASE_SEQ.compareAndSet(-1, 0)) {
                    INCREASE_SEQ.set(sessionDao.getCount());
                }
            }
            seq = INCREASE_SEQ.incrementAndGet();
            String sessionId = seq.toString();// TODO(至少要确保不会重复)
            session.setSessionId(sessionId);
            Calendar calendar = Calendar.getInstance();
            calendar.add(DEFAULT_TIMEOUT_FIELD, DEFAULT_TIMEOUT_DURATION);
            session.setDeadline(calendar.getTime());
            sessionDao.addSession(session);
            
            sqlSession.commit();
            sqlSession.close();
            // 告知新的身份令牌
            response.getHeaders().set("Auth", sessionId);
            return RestResult.asSuccess();
        }
    }

    /**
     * 获取请求绑定的有效的会话（session）
     */
    public static final RestResult<?> getSession(Request request) {
        String sessionId = getAuthKey(request);
        if (StringUtils.isEmpty(sessionId)) {
            return RestResult.asError(Status.CLIENT_ERROR_FORBIDDEN.getCode(), "session not exist");
        }

        try (SqlSession sqlSession = SqlSessionManager.getSession()) {
            SessionDao sessionDao = sqlSession.getMapper(SessionDao.class);
            SessionDO session = sessionDao.getBySessionId(sessionId);
            if (session != null) {
                if (session.getDeadline().after(new Date())) {
                    return RestResult.asSuccess(session);
                } else {
                    sessionDao.deleteBySessionId(sessionId);
                    sqlSession.commit();
                    return RestResult.asError(Status.CLIENT_ERROR_FORBIDDEN.getCode(), "session timeout");
                }
            } else {
                return RestResult.asError(Status.CLIENT_ERROR_FORBIDDEN.getCode(), "session not exist");
            }
        }
    }

    /**
     * 获取请求的认证令牌
     */
    private static String getAuthKey(Request request) {
        return request.getHeaders().getFirstValue("Auth");
    }

}
