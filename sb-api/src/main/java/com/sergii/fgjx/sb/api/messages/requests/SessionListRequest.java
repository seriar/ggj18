package com.sergii.fgjx.sb.api.messages.requests;

public class SessionListRequest extends Request {
    private final int size;

    public SessionListRequest(String requester, String id, int size) {
        super(requester, id);
        this.size = size;
    }
    public SessionListRequest(String requester, int size) {
        super(requester);
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "SessionListRequest{" +
                "size=" + size +
                ", requester='" + requester + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
