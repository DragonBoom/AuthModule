package indi.auth.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import indi.auth.entity.SessionDO;

public interface SessionDao {
    
    @Select("select session_id as sessionId, user_id as userId, deadline from session where session_id = #{sessionId};")
    SessionDO getBySessionId(String sessionId);
    
    @Insert("insert into session (session_id, user_id, deadline) values (#{sessionId}, #{userId}, #{deadline});")
    void addSession(SessionDO session);
    
    @Delete("delete from session where session_id = #{sessionId};")
    void deleteBySessionId(String sessionId);
    
    @Delete("delete from session where deadline < now();")
    void deleteTimeoutSession();
    
    @Select("select count(1) from session")
    Integer getCount();
    
    @Select("select * from session limit #{index}, #{offset}")
    List<SessionDO> query(@Param("index") int index, @Param("offset") int offset);
    
}
