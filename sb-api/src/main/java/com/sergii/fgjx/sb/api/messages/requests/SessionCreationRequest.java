package com.sergii.fgjx.sb.api.messages.requests;

public class SessionCreationRequest extends Request {

    public SessionCreationRequest(String requester, String id) {
        super(requester, id);
    }

    public SessionCreationRequest(String requester) {
        super(requester);
    }

    @Override
    public String toString() {
        return "SessionCreationRequest{" +
                "requester='" + requester + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
