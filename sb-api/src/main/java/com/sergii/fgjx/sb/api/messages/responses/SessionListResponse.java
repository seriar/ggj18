package com.sergii.fgjx.sb.api.messages.responses;

import java.util.Set;

public class SessionListResponse extends Response {
    private final Set<String> sessionIds;
    private final int total;

    public SessionListResponse(String id, Set<String> sessionIds, int total) {
        super(id);
        this.sessionIds = sessionIds;
        this.total = total;
    }

    public Set<String> getSessionIds() {
        return sessionIds;
    }

    public int getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "SessionListResponse{" +
                "sessionIds=" + sessionIds +
                ", total=" + total +
                ", id='" + id + '\'' +
                '}';
    }
}
