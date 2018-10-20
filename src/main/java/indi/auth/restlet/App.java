package indi.auth.restlet;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;

import indi.auth.restlet.resource.AuthResource;
import indi.auth.restlet.resource.InfoResource;
import indi.auth.restlet.resource.SessionResource;
import indi.auth.restlet.resource.UserInfo;
import indi.auth.restlet.resource.UserResource;
import indi.auth.scheduled.ScheduledTimeoutSessionHandler;

public class App {

    public static void main(String[] args) throws Exception {
        // conf
        // 添加pojo类<->json的转化器
        Engine.getInstance().getRegisteredConverters().add(new JsonConverter());

        // use component to manage a set of Connectors, VirtualHosts, Services and Applications
        Component component = new Component();
        // add server
        component.getServers().add(Protocol.HTTP, 8082);
        // attach pathTemplate to route in default host
        component.getDefaultHost().attach("/auth/user", UserResource.class);
        component.getDefaultHost().attach("/auth/user/info", UserInfo.class);
        component.getDefaultHost().attach("/auth/session", SessionResource.class);
        component.getDefaultHost().attach("/auth/info", InfoResource.class);
        component.getDefaultHost().attach("/auth", AuthResource.class);
        component.start();

        // 启动定时任务
        ScheduledTimeoutSessionHandler scheduledTimeoutSessionHandler = new ScheduledTimeoutSessionHandler();
        scheduledTimeoutSessionHandler.setDaemon(true);
        scheduledTimeoutSessionHandler.start();
    }

}
