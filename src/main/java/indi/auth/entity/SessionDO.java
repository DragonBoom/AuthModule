package indi.auth.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SessionDO implements Serializable {
    private static final long serialVersionUID = 5623114260635251939L;

    private String sessionId;

    private Long userId;

    private Date deadline;
}
