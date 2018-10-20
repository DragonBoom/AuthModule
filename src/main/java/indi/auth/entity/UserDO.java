package indi.auth.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDO implements Serializable {
    private static final long serialVersionUID = -5636084708995107555L;

    private Long id;
    
    private String username;
    
    private String password;
    
    private String avatar;

}
