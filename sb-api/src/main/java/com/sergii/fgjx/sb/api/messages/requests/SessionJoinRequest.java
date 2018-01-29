package com.sergii.fgjx.sb.api.messages.requests;

public class SessionJoinRequest extends Request {
    private final String session;

    public SessionJoinRequest(String requester, String id, String session) {
        super(requester, id);
        this.session = session;
    }

    public SessionJoinRequest(String requester, String session) {
        super(requester);
        this.session = session;
    }

    public String getSession() {
        return session;
    }

    @Override
    public String toString() {
        return "SessionJoinRequest{" +
                "session='" + session + '\'' +
                ", requester='" + requester + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
