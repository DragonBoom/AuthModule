package indi.auth.restlet.resource;

import org.junit.jupiter.api.Test;

class SessionResourceTest {

    @Test
    void test() {
        SessionResource sessionResource = new SessionResource();
        System.out.println(sessionResource.createSession());
    }

}
