package indi.auth.data;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.BeanUtils;

import indi.auth.entity.SessionDO;
import indi.auth.entity.UserDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthInfo implements Serializable {
    private static final long serialVersionUID = 8411721637191077740L;

    private String username;

    private String avatar;

    private String sessionId;

    private Date deadline;

    public static AuthInfo of(UserDO user, SessionDO session) {
        AuthInfo authInfo = new AuthInfo();
        if (user != null) {
            BeanUtils.copyProperties(user, authInfo);
        }
        if (session != null) {
            BeanUtils.copyProperties(session, authInfo);
        }
        return authInfo;
    }
}
