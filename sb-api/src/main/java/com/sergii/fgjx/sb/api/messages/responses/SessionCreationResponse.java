package com.sergii.fgjx.sb.api.messages.responses;

public class SessionCreationResponse extends Response {

    final String sessionId;
    final boolean success;

    public SessionCreationResponse(String id, String sessionId, boolean success) {
        super(id);
        this.sessionId = sessionId;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getSessionId() {
        return sessionId;
    }

    @Override
    public String toString() {
        return "SessionCreationResponse{" +
                "sessionId='" + sessionId + '\'' +
                ", success=" + success +
                ", id='" + id + '\'' +
                '}';
    }
}
