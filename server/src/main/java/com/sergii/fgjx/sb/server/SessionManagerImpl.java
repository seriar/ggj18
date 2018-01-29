package com.sergii.fgjx.sb.server;

import java.util.HashMap;
import java.util.Set;

public class SessionManagerImpl implements SessionManager {

    private final HashMap<String, Session> sessions;
    private final HashMap<String, Session> pendingSessions;

    public SessionManagerImpl() {
        this.sessions = new HashMap<>();
        this.pendingSessions = new HashMap<>();
    }

    @Override
    public Session createSession() {
        final Session session = new Session();
        sessions.put(session.getId(), session);
        pendingSessions.put(session.getId(), session);
        return session;
    }

    @Override
    public Set<String> listSessions() {
        return sessions.keySet();
    }

    @Override
    public Set<String> listPendingSessions() {
        return pendingSessions.keySet();
    }

    @Override
    public Session removeSession(String id) {
        pendingSessions.remove(id);
        return sessions.remove(id);
    }

    @Override
    public void joinSession(String sessionId, String requesterId, String requestId) {
        final Session session = sessions.get(sessionId);
        if (session != null) {
            session.joinSession(requesterId, requestId);
            if (session.isFull()) {
                pendingSessions.remove(sessionId);
            }
        }
    }

    @Override
    public Session getSession(String id) {
        return pendingSessions.get(id);
    }
}
