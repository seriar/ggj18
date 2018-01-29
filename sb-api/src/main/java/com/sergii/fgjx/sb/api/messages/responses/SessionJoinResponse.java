package com.sergii.fgjx.sb.api.messages.responses;

public class SessionJoinResponse extends Response {
    private final String sessionTopic;
    private final boolean success;

    public SessionJoinResponse(String id, String sessionTopic, boolean success) {
        super(id);
        this.sessionTopic = sessionTopic;
        this.success = success;
    }

    public String getSessionTopic() {
        return sessionTopic;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "SessionJoinResponse{" +
                "success=" + success +
                ", id='" + id + '\'' +
                '}';
    }
}
