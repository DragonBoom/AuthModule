package indi.auth.restlet.resource;

import org.restlet.resource.Post;

import indi.auth.service.Service;
import indi.core.rest.result.RestResult;

public class SessionResource extends MyResource {

    /**
     * 注册会话，若已存在会话则删除旧会话并创建新的会话
     */
    @Post
    public synchronized RestResult<?> createSession() {
        RestResult<?> createSessionResult = Service.createSession(super.getRequest(), super.getResponse());
        return createSessionResult;
    }
}
