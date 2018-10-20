package indi.auth.scheduled;

import java.util.concurrent.TimeUnit;

import org.apache.ibatis.session.SqlSession;

import indi.auth.dao.SessionDao;
import indi.auth.sql.SqlSessionManager;
import indi.core.util.LoggerUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * 定时清理数据库中过时的会话
 * 
 * @author DragonBoom
 *
 */
public class ScheduledTimeoutSessionHandler extends Thread {
    private TimeUnit unit = TimeUnit.HOURS;
    private long duration = 1L;

    @Getter
    @Setter
    private volatile boolean retire = false;

    @Override
    public void run() {
        LoggerUtils.getLogger().info("已启动定时会话清理器");
        while (!retire) {
            try {
                Thread.sleep(unit.toMillis(duration));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SqlSession sqlSession = SqlSessionManager.getSession();
            SessionDao sessionDao = sqlSession.getMapper(SessionDao.class);
            // TODO
            sessionDao.deleteTimeoutSession();
            sqlSession.commit();
        }
    }

}
