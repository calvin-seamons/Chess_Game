package Handlers;

import java.util.UUID;

public abstract class BaseHandler {

    protected String createAuthToken() {
        return UUID.randomUUID().toString();
    }

    // Any other common methods can go here

}