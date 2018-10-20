package indi.auth.data;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;

import indi.auth.entity.UserDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO implements Serializable {
    private static final long serialVersionUID = -5636084708995107525L;

    private Long id;

    private String username;

    private String avatar;

    public static UserDTO of(UserDO entity) {
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

}
