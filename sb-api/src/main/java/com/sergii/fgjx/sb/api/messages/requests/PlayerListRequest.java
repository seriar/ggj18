package com.sergii.fgjx.sb.api.messages.requests;

public class PlayerListRequest extends Request {
    private final String sessionId;

    public PlayerListRequest(String requester, String id, String sessionId) {
        super(requester, id);
        this.sessionId = sessionId;
    }

    public PlayerListRequest(String requester, String sessionId) {
        super(requester);
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    @Override
    public String toString() {
        return "PlayerListRequest{" +
                "sessionId='" + sessionId + '\'' +
                ", requester='" + requester + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
