package com.sergii.fgjx.sb.server;

import java.util.Set;

public interface SessionManager {

    Session createSession();
    Set<String> listSessions();
    Set<String> listPendingSessions();
    Session removeSession(String id);
    void joinSession(String sessionId, String requesterId, String requestId);
    Session getSession(String id);

}
