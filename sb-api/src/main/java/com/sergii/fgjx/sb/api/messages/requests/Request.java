package com.sergii.fgjx.sb.api.messages.requests;

import java.util.UUID;

public class Request {
    protected final String requester;
    protected final String id;

    public Request(String requester, String id) {
        this.requester = requester;
        this.id = id;
    }

    public Request(String requester) {
        this.requester = requester;
        this.id = UUID.randomUUID().toString();
    }

    public String getRequester() {
        return requester;
    }

    public String getId() {
        return id;
    }
}
