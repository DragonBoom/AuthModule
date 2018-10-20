package indi.auth.sql;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import indi.auth.dao.SessionDao;
import indi.auth.dao.UserDao;
import lombok.Getter;

public class SqlSessionManager {
    private static SqlSessionFactory sqlSessionFactory;
    private static final String username = "root";
    private static final String password = "!qQ1312449403";
    private static final String driverClassName = "com.mysql.cj.jdbc.Driver";
    private static final String jdbcUrl = "jdbc:mysql://localhost:3306/auth?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC&useSSL=false";
    private static final String envId = "firstEnv";
    @Getter
    private static Configuration configuration;

    static {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
            Runtime.getRuntime().exit(-1);
        }
    }

    public static void init() {
        // datasource
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setJdbcUrl(jdbcUrl);
        DataSource dataSource = new HikariDataSource(hikariConfig);
        // transactional
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        // env
        Environment environment = new Environment(envId, transactionFactory, dataSource);
        configuration = new Configuration(environment);
        // add mapper TODO
        configuration.addMapper(UserDao.class);
        configuration.addMapper(SessionDao.class);
        // final
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

    public static SqlSession getSession() {
        return sqlSessionFactory.openSession();
    }

}
