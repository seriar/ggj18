package com.sergii.fgjx.sb.api.messages.requests;

public class RoleRequest extends Request {
    public RoleRequest(String requester, String id) {
        super(requester, id);
    }

    public RoleRequest(String requester) {
        super(requester);
    }
}
